package io.github.wesleyosantos91.resource;

import io.github.wesleyosantos91.dto.PersonDto;
import io.github.wesleyosantos91.event.person.Person;
import io.github.wesleyosantos91.mapper.PersonMapper;
import io.github.wesleyosantos91.producer.KafkaPersonProducer;
import io.github.wesleyosantos91.producer.PersonProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/persons")
@RequiredArgsConstructor
public class PersonResource {

    private final PersonProducer producer;
    private final KafkaPersonProducer kafkaPersonProducer;
    private final PersonMapper mapper;

    @PostMapping(value = "/publish")
    public ResponseEntity<?> sendMessageToKafkaTopic(@RequestBody PersonDto person) {

        kafkaPersonProducer.send(mapper.parseDtoToEvent(person));
        return ResponseEntity.accepted().build();
    }
}
