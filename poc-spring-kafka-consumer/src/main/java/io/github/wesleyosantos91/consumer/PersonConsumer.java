package io.github.wesleyosantos91.consumer;

import io.github.wesleyosantos91.domain.PersonDomain;
import io.github.wesleyosantos91.dto.PersonDto;
import io.github.wesleyosantos91.event.person.Person;
import io.github.wesleyosantos91.mapper.PersonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonConsumer extends AbstractConsumer<Person, PersonDomain, PersonDto, PersonMapper> {

    public PersonConsumer(PersonMapper mapper) {
        super(mapper);
    }

    @Override
    void processar(PersonDomain personDomain) {
        log.info("event with cpf {}", personDomain.getCpf());
    }
}
