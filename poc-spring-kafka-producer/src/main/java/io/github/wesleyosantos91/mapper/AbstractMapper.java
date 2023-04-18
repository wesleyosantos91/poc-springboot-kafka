package io.github.wesleyosantos91.mapper;

public interface AbstractMapper<Event, Domain, Dto> {


    Domain parseEventoToDomain(Event event);
    Domain parseDtoToDomain(Dto dto);

    Event parseDomainToEvent(Domain Domain);
    Event parseDtoToEvent(Dto dto);

    Dto parseDomainToDto(Domain Domain);
    Dto parseEventToDto(Event event);
}
