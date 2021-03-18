package io.github.wesleyosantos91.config;

import io.github.wesleyosantos91.exception.Exceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.springframework.kafka.support.KafkaHeaders.DLT_ORIGINAL_TOPIC;

@EnableKafka
@Configuration
@Slf4j
@RequiredArgsConstructor
public class KafkaConfig {

    static final FixedBackOff RETRY_3X = new FixedBackOff(0, 2);
    static final FixedBackOff NONE = new FixedBackOff(0, 0);

    private static final String NONE_HEADER = "__$$none";
    private static final int ANY_PARTITION = -1;

    private final Exceptions exceptions;

    @Value("${spring.kafka.listener.missing-topics-fatal}")
    Boolean missingTopicsFatal;

    @Value("${app.kafka.dlt.retry.topics}")
    int retryTopicsCount;

    @Value("${app.kafka.dlt.retry.topics.pattern}")
    String retryTopicsPattern;

    @Value("${app.kafka.dlt.retry.topic.first}")
    String retryFirstTopic;

    @Value("${app.kafka.consumer.topics}")
    String topicoOriginal;

    @Value("${app.kafka.dlt.topic}")
    String dltTopic;

    @Bean
    public ConsumerFactory<?, ?> consumerFactory(KafkaProperties properties) {
        return new DefaultKafkaConsumerFactory<>(properties.buildConsumerProperties());
    }

    @Bean
    public BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> mainResolver() {

        return (r, e) -> {
            TopicPartition result = new TopicPartition(dltTopic, ANY_PARTITION);
            final boolean recoverable = isRecovered(e);

            if (recoverable) {
                Optional<String> origin = originTopic(r.headers()).or(() -> Optional.of(NONE_HEADER));
                log.info("topic origin {}", origin);

                String destiny = origin
                                    .filter(topic -> !topic.matches(retryTopicsPattern))
                                    .map(t -> retryFirstTopic)
                                    .orElse(dltTopic);

                log.info("destiny topic of the registry {}", destiny);
                result = new TopicPartition(destiny, ANY_PARTITION);
            }

            return result;
        };
    }

    @Bean
    public SeekToCurrentErrorHandler mainErrorHandler(@Qualifier("mainResolver") BiFunction<ConsumerRecord<?, ?>,
            Exception, TopicPartition> resolver, KafkaTemplate<?, ?> template) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template, resolver);
        SeekToCurrentErrorHandler handler = new SeekToCurrentErrorHandler(recoverer, RETRY_3X);
        exceptions.getNotRecovered().forEach(e -> handler.addNotRetryableException(e));
        return handler;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GenericRecord>>
        kafkaListenerContainerFactory(@Qualifier("mainErrorHandler") SeekToCurrentErrorHandler errorHandler,
            KafkaProperties properties, ConsumerFactory<String, GenericRecord> factory){

        ConcurrentKafkaListenerContainerFactory<String, GenericRecord> listener =
                new ConcurrentKafkaListenerContainerFactory<>();

        listener.setConsumerFactory(factory);
        listener.setErrorHandler(errorHandler);
        listener.getContainerProperties().setMissingTopicsFatal(missingTopicsFatal);
        listener.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        listener.getContainerProperties().setSyncCommits(Boolean.TRUE);

        return listener;
    }

    @Bean
    public BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> retryResolver() {

        return (r, e) -> {
            Optional<String> origin = originTopic(r.headers())
                                        .filter(t -> t.matches(retryTopicsPattern))
                                        .or(() -> Optional.of(retryFirstTopic));

            log.info("topic origin {}", origin.get());

            String destiny = origin
                                .filter(topic -> topic.matches(retryTopicsPattern))
                                .map(t -> t.substring(t.lastIndexOf("-")))
                                .map(n -> n.split("-"))
                                .map(n -> n[1])
                                .map(Integer::parseInt)
                                .filter(n -> n < retryTopicsCount)
                                .map(n -> origin.get().substring(0, origin.get().lastIndexOf("-")) + "-" + (n + 1))
                                .orElse(dltTopic);

            log.info("destiny topic of the registry {}", destiny);

            return new TopicPartition(destiny, ANY_PARTITION);
        };
    }

    @Bean
    public SeekToCurrentErrorHandler retryErrorHandler(@Qualifier("retryResolver") BiFunction<ConsumerRecord<?, ?>,
            Exception, TopicPartition> resolver, KafkaTemplate<?, ?> template) {

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(template, resolver);
        SeekToCurrentErrorHandler handler = new SeekToCurrentErrorHandler(recoverer, NONE);
        return handler;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GenericRecord>>
    retryKafkaListenerContainerFactory(@Qualifier("retryErrorHandler") SeekToCurrentErrorHandler errorHandler,
            KafkaProperties properties, ConsumerFactory<String, GenericRecord> factory){

        ConcurrentKafkaListenerContainerFactory<String, GenericRecord> listener = new ConcurrentKafkaListenerContainerFactory<>();

        listener.setConsumerFactory(factory);
        listener.setErrorHandler(errorHandler);

        listener.getContainerProperties().setMissingTopicsFatal(missingTopicsFatal);
        listener.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        listener.getContainerProperties().setSyncCommits(Boolean.TRUE);

        return listener;
    }

    private boolean isRecovered(Exception e) {

        boolean result;
        Throwable cause = ExceptionUtils.getRootCause(e);
        log.error(cause.getMessage(), cause);
        result = exceptions.getRecovered().contains(cause.getClass());
        log.info("{} It is {} Recovered", cause.getClass(), (result ? "" : "Not -"));

        return result;
    }

    private Optional<String> originTopic(Headers headers) {

        return Optional.ofNullable(headers.lastHeader(DLT_ORIGINAL_TOPIC))
                        .map(Header::value)
                        .map(bytes -> new String(bytes));

    }
}
