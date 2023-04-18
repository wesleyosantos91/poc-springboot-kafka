package io.github.wesleyosantos91.consumer;

import io.github.wesleyosantos91.domain.PersonDomain;
import io.github.wesleyosantos91.dto.PersonDto;
import io.github.wesleyosantos91.event.person.Person;
import io.github.wesleyosantos91.mapper.PersonMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonConsumer extends AbstractConsumer<Person, PersonDomain, PersonDto, PersonMapper> {

    public PersonConsumer(PersonMapper mapper) {
        super(mapper);
    }
}
