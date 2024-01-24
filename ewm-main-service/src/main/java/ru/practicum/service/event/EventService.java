package ru.practicum.service.event;

import ru.practicum.dto.event.*;
import ru.practicum.entity.event.Event;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createForInitiator(long userId, NewEventDto newEventDto);

    Event updateAndGetEntity(Event event);

    EventFullDto updateForAdminByEventId(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    EventFullDto updateEventForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    Event checkAndGetEntityById(long id);

    EventFullDto getForInitiatorByInitiatorIdAndEventId(long userId, long eventId);

    EventFullDto getForPublicUsersById(long id);

    List<EventFullDto> getAllForAdmin(List<Long> usersIds, List<String> states, List<Long> categoryIds, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<EventShortDto> getAllForInitiatorByInitiatorId(int from, int size, long userId);

    List<EventShortDto> getAllForPublicUsers(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size, HttpServletRequest request);

    Event checkInitiatorIdIsLinkedToEventId(long initiatorId, long eventId);
}
