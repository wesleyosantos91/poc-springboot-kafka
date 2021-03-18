package io.github.wesleyosantos91.service;

import io.github.wesleyosantos91.domain.person.Person;
import io.github.wesleyosantos91.exception.BandRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@RequiredArgsConstructor
public class Producer {

    @Value("${app.kafka.producer.topics}")
    private String TOPIC;

    private final KafkaTemplate<String, Person> kafkaTemplate;

    public void send(Person person) {
        try {
            ListenableFutureCallback<SendResult<String, Person>> futureCallback = new ListenableFutureCallback<SendResult<String, Person>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    log.error("Failed to send event {}", throwable.getCause());
                    throw new RuntimeException(throwable);
                }

                @Override
                public void onSuccess(SendResult<String, Person> sendResult) {
                    log.info("Success to send event {}", sendResult.getRecordMetadata().toString());
                }
            };

            this.kafkaTemplate.send(this.TOPIC, person.getCpf(), person).addCallback(futureCallback);

        } catch (BandRequestException ex) {
            log.error(ex.getMessage(), ex);
            throw new BandRequestException(ex.getLocalizedMessage(), ex);
        }
    }

}
