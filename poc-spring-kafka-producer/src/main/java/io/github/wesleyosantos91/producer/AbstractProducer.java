package io.github.wesleyosantos91.producer;

import de.huxhorn.sulky.ulid.ULID;
import io.github.wesleyosantos91.exception.BusinessException;
import io.github.wesleyosantos91.mapper.AbstractMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AbstractProducer <Event, Domain, Dto, Mapper extends AbstractMapper<Event, Domain, Dto>> {

    @Value("${app.kafka.topic}")
    private String TOPIC;

    private final KafkaTemplate<String, Event> kafkaTemplate;
    private final Mapper mapper;

    public void send(Event event) {
        try {

            ULID ulid = new ULID();
            Message<Event> message = createMessageWithHeaders(ulid.nextULID(), event);

            this.kafkaTemplate.send(message).whenComplete((result, ex) -> {
                if (Objects.nonNull(ex)) {
                    log.error("Failed to send event {}, with messageID {}", ex.getCause(), ulid);
                    throw new RuntimeException(ex);
                }

                log.info("Success to send event {}, with messageID {}, Event: {}", result.getRecordMetadata().toString(), ulid, mapper.parseEventToDto(event));
            });

        } catch (BusinessException ex) {
            log.error(ex.getMessage(), ex);
            throw new BusinessException(ex.getMessage(), ex);
        }
    }

    private Message<Event> createMessageWithHeaders(String ulid, Event event) {
        return MessageBuilder.withPayload(event)
                .setHeader("hash", event.hashCode())
                .setHeader("version", "1.0.0")
                .setHeader("endOfLife", LocalDate.now().plusDays(1L))
                .setHeader("type", "fct")
                .setHeader("cid", ulid)
                .setHeader(KafkaHeaders.TOPIC, this.TOPIC)
                .setHeader(KafkaHeaders.KEY, ulid)
                .build();
    }
}
