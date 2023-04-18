package io.github.wesleyosantos91.producer;

import io.github.wesleyosantos91.domain.PersonDomain;
import io.github.wesleyosantos91.dto.PersonDto;
import io.github.wesleyosantos91.event.person.Person;
import io.github.wesleyosantos91.mapper.PersonMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PersonProducer extends AbstractProducer<Person, PersonDomain, PersonDto, PersonMapper> {

    public PersonProducer(KafkaTemplate<String, Person> kafkaTemplate, PersonMapper mapper) {
        super(kafkaTemplate, mapper);
    }
}
