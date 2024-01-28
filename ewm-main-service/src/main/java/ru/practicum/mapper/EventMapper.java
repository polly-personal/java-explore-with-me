package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.entity.event.Event;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class EventMapper {
    public Event toEvent(NewEventDto newEventDto) {
        Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(LocationMapper.toLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();

        log.info("🔀\nDTO={} сконвертирован в \nJPA-сущность={}", newEventDto, event);
        return event;
    }

    public EventFullDto toEventFullDto(Event event) {
        EventFullDto eventFullDto = EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .build();

        log.info("🔀 \nJPA-сущность={} сконвертирована в \nDTO={}", event, eventFullDto);
        return eventFullDto;
    }

    public List<EventFullDto> toEventFullDtos(List<Event> events) {
        List<EventFullDto> eventFullDtos = events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());

        log.info("🔀 \nсписок JPA-сущностей={} сконвертирован в \nсписок DTO={}", events, eventFullDtos);
        return eventFullDtos;
    }

    public EventShortDto toEventShortDto(Event event) {
        EventShortDto eventShortDto = EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();

        log.info("🔀 \nJPA-сущность={} сконвертирована в \nDTO={}", event, eventShortDto);
        return eventShortDto;
    }

    public List<EventShortDto> toEventShortDtos(List<Event> events) {
        List<EventShortDto> eventShortDtos = events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        log.info("🔀 \nсписок JPA-сущностей={} сконвертирован в \nсписок DTO={}", events, eventShortDtos);
        return eventShortDtos;
    }
}
