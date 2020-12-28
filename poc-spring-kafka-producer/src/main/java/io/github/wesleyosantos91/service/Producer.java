package io.github.wesleyosantos91.service;

import io.github.wesleyosantos91.domain.person.Person;
import io.github.wesleyosantos91.exception.BandRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class Producer {

    @Value("${topic.name}")
    private String TOPIC;

    private final KafkaTemplate<String, Person> kafkaTemplate;

    public void send(Person person) {
        try {
            this.kafkaTemplate.send(this.TOPIC, person.getCpf(), person);
            log.info(String.format("Produced person -> %s", person));
        } catch (BandRequestException ex) {
            log.error(ex.getMessage(), ex);
            throw new BandRequestException(ex.getLocalizedMessage(), ex);
        }
    }

}
