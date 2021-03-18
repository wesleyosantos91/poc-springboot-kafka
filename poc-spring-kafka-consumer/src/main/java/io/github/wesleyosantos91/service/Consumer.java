package io.github.wesleyosantos91.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.net.ConnectException;

@Service
@Slf4j
public class Consumer {

    @KafkaListener(
            id = "main-kafka-listener",
            topics = "${app.kafka.consumer.topics}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumer(@Payload ConsumerRecord<String, GenericRecord> consumerRecord, Acknowledgment ack) throws ConnectException {
        try {

            log.info("Processar registro {}", consumerRecord.value());

            log.info("key: " + consumerRecord.key());
            log.info("Headers: " + consumerRecord.headers());
            log.info("topic: " + consumerRecord.topic());
            log.info("Partion: " + consumerRecord.partition());
            log.info("Person: " + consumerRecord.value());
            throw new ConnectException();
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(
            id = "retry-kafka-listener",
            topicPattern = "${app.kafka.dlt.retry.topics.pattern}",
            containerFactory = "retryKafkaListenerContainerFactory",
            properties = {
                    "fetch.min.bytes=${app.kafka.dlt.retry.min.bytes}",
                    "fetch.max.wait.ms=${app.kafka.dlt.retry.max.wait.ms}"
            }
    )
    public void retry(@Payload ConsumerRecord<String, GenericRecord> consumerRecord, Acknowledgment ack) throws Exception{

        try {
            log.info("Reprocessar registro {}, topico {}", consumerRecord.value(), consumerRecord.topic());


            log.info("key: " + consumerRecord.key());
            log.info("Headers: " + consumerRecord.headers());
            log.info("topic: " + consumerRecord.topic());
            log.info("Partion: " + consumerRecord.partition());
            log.info("Person: " + consumerRecord.value());
            throw new ConnectException();
        } finally {
            ack.acknowledge();
        }
    }
}
