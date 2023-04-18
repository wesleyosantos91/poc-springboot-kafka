package io.github.wesleyosantos91.consumer;

import io.github.wesleyosantos91.mapper.AbstractMapper;
import io.github.wesleyosantos91.mapper.PersonMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;

@Slf4j
@AllArgsConstructor
public abstract class AbstractConsumer <Event, Domain, Dto, Mapper extends AbstractMapper<Event, Domain, Dto>> {

    private Mapper mapper;

    @KafkaListener(topics = "${app.kafka.topic}")
    public void consumeMessage(@Payload ConsumerRecord<String, Event> record, Acknowledgment acknowledgment) {

        log.info("process record {}", mapper.parseEventoToDomain(record.value()));

        log.info("key: " + record.key());
        log.info("Headers: " + record.headers());
        log.info("topic: " + record.topic());
        log.info("Partion: " + record.partition());
        log.info("Person: " + record.value());


        // Confirme o processamento da mensagem manualmente usando o Acknowledgment
        acknowledgment.acknowledge();
    }
}
