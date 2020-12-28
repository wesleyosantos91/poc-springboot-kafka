package io.github.wesleyosantos91.resource;

import io.github.wesleyosantos91.service.Producer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.wesleyosantos91.domain.person.Person;

@RestController
@RequestMapping(value = "/persons")
@RequiredArgsConstructor
public class PersonResource {

    private final Producer producer;

    @PostMapping(value = "/publish")
    public ResponseEntity<?> sendMessageToKafkaTopic(@RequestBody Person person) {
        producer.send(person);
        return ResponseEntity.accepted().build();
    }
}
