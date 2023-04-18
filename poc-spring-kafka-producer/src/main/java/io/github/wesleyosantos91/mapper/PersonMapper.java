package io.github.wesleyosantos91.mapper;

import io.github.wesleyosantos91.domain.PersonDomain;
import io.github.wesleyosantos91.dto.PersonDto;
import io.github.wesleyosantos91.event.person.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper extends AbstractMapper<Person, PersonDomain, PersonDto>{
}
