package ru.practicum.service.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.ViewStatsDto;
import ru.practicum.dto.event.*;
import ru.practicum.entity.event.Event;
import ru.practicum.entity.event.EventState;
import ru.practicum.entity.request.RequestStatus;
import ru.practicum.exception.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.request.ConfirmedRequestShortDto;
import ru.practicum.repository.request.RequestsRepository;
import ru.practicum.service.categories.CategoryService;
import ru.practicum.service.user.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    private final RequestsRepository requestsRepository;

    private final StatsClient statsClient;

    @Transactional
    public EventFullDto createForInitiator(long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new MainExceptionIncorrectDateTime("Field: eventDate. Error: дата и время на которые намечено " +
                    "событие не может быть раньше, чем через два часа от текущего момента. Value: " + newEventDto.getEventDate());
        }
        if (newEventDto.getRequestModeration() == null) newEventDto.setRequestModeration(true);
        if (newEventDto.getPaid() == null) newEventDto.setPaid(false);
        if (newEventDto.getParticipantLimit() == null) newEventDto.setParticipantLimit(0);

        Event event = EventMapper.toEvent(newEventDto);

        event.setCategory(categoryService.checkAndGetEntityById(newEventDto.getCategory()));
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userService.checkAndGetEntityById(userId));
        event.setState(EventState.PENDING);

        Event resultEvent = eventRepository.save(event);

        log.info("🟩 создано событие={}", resultEvent);
        return EventMapper.toEventFullDto(resultEvent);
    }

    @Transactional
    public Event updateAndGetEntity(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public EventFullDto updateForAdminByEventId(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = checkAndGetEntityById(eventId);

        EventState currentEventState = event.getState();
        if (currentEventState.equals(EventState.PUBLISHED) || currentEventState.equals(EventState.CANCELED)) {
            throw new MainExceptionImpossibleToCreateOrUpdateEntity("Cannot publish the event because it's not in the right " +
                    "state: " + currentEventState);
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(UpdateEventAdminRequest.StateAction.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEventAdminRequest.getStateAction().equals(UpdateEventAdminRequest.StateAction.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventAdminRequest.getEventDate() != null && !event.getEventDate().equals(updateEventAdminRequest.getEventDate())) {
            checkNewEventDate(updateEventAdminRequest.getEventDate(), 1);
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        Event repoResult = eventRepository.save(changeEvent(event, updateEventAdminRequest));

        log.info("🟪 обновлено событие для администратора={}", repoResult);
        return EventMapper.toEventFullDto(repoResult);
    }

    @Transactional
    public EventFullDto updateForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        checkAndGetEntityById(eventId);
        userService.checkAndGetEntityById(initiatorId);

        Event event = checkInitiatorIdIsLinkedToEventId(initiatorId, eventId);

        EventState currentEventState = event.getState();
        if (currentEventState.equals(EventState.PUBLISHED)) {
            throw new MainExceptionImpossibleToCreateOrUpdateEntity("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (event.getState().equals(EventState.PENDING) && updateEventUserRequest.getStateAction().equals(UpdateEventUserRequest.StateAction.CANCEL_REVIEW)) {
                event.setState(EventState.CANCELED);
            }
            if (event.getState().equals(EventState.CANCELED) && updateEventUserRequest.getStateAction().equals(UpdateEventUserRequest.StateAction.SEND_TO_REVIEW)) {
                event.setState(EventState.PENDING);
            }
        }

        if (updateEventUserRequest.getEventDate() != null && !event.getEventDate().equals(updateEventUserRequest.getEventDate())) {
            checkNewEventDate(updateEventUserRequest.getEventDate(), 2);
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        Event repoResult = eventRepository.save(changeEvent(event, updateEventUserRequest));

        log.info("🟪 обновлено событие для инициатора={}", repoResult);
        return EventMapper.toEventFullDto(repoResult);
    }

    public Event checkAndGetEntityById(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new MainExceptionIdNotFound("Event with id=" + id + " was not found"));
    }

    public EventFullDto getForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId) {
        checkAndGetEntityById(eventId);
        userService.checkAndGetEntityById(initiatorId);

        Event event = checkInitiatorIdIsLinkedToEventId(initiatorId, eventId);

        log.info("🟦 выдано событие={}", event);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto getForPublicUsersById(long id) {
        Event event = checkAndGetEntityById(id);

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new MainExceptionImpossibleToPublicGetEntity("событие должно быть опубликовано");

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(requestsRepository.countByEventIdAndStatus(id, RequestStatus.CONFIRMED));
        List<ViewStatsDto> viewStatsDtos = getViews(List.of(event));
        if (!viewStatsDtos.isEmpty()) eventFullDto.setViews(viewStatsDtos.get(0).getHits());

        log.info("🟦 выдано событие={}", eventFullDto);
        return eventFullDto;
    }

    public List<EventFullDto> getAllForAdmin(List<Long> usersIds, List<String> states, List<Long> categoryIds, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        if (usersIds == null) usersIds = new ArrayList<>();
        if (states == null) states = new ArrayList<>();
        if (categoryIds == null) categoryIds = new ArrayList<>();
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = LocalDateTime.now().plusYears(10);
        if (rangeStart.isAfter(rangeEnd))
            throw new MainExceptionIncorrectDateTime("параметр rangeStart: " + rangeStart + " НЕ может идти после параметра rangeEnd: " + rangeEnd);

        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Event> eventPages = eventRepository.getAllForAdmin(usersIds, states, categoryIds, rangeStart, rangeEnd,
                pageRequest);

        List<ConfirmedRequestShortDto> requestRepoResult = requestsRepository.getCountConfirmedRequestsForAllEvents();
        Map<Long, Integer> countPublishedRequestsForAllEvents = new HashMap<>();
        for (ConfirmedRequestShortDto counts : requestRepoResult) {
            countPublishedRequestsForAllEvents.put(counts.getEventId(), counts.getCount());
        }

        List<Event> events = eventPages.toList();
        List<EventFullDto> eventFullDtos = EventMapper.toEventFullDtos(events);
        List<ViewStatsDto> viewStatsDtos = getViews(events);
        eventFullDtos.stream()
                .forEach(eventShortDto -> {
                    if (!viewStatsDtos.isEmpty()) {
                        Optional<ViewStatsDto> viewStatsDto = viewStatsDtos.stream()
                                .filter(viewStats -> viewStats.getUri().equals("/events" + eventShortDto.getId()))
                                .findFirst();
                        if (viewStatsDto.isPresent()) eventShortDto.setViews(viewStatsDto.get().getHits());
                    }

                    if (countPublishedRequestsForAllEvents.containsKey(eventShortDto.getId())) {
                        eventShortDto.setConfirmedRequests(countPublishedRequestsForAllEvents.get(eventShortDto.getId()));
                    }
                });

        log.info("🟦 для администратора выдан список событий={}", eventFullDtos);
        return eventFullDtos;
    }

    public List<EventShortDto> getAllForInitiatorByInitiatorId(int from, int size, long initiatorId) {
        userService.checkAndGetEntityById(initiatorId);

        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Event> events = eventRepository.findAllByInitiatorId(initiatorId, pageRequest);

        log.info("🟦 для инициатора событий с id={} выдан список событий={}", initiatorId, events.toList());
        return EventMapper.toEventShortDtos(events.toList());
    }

    public List<EventShortDto> getAllForPublicUsers(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        if (categoryIds == null) categoryIds = new ArrayList<>();
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = LocalDateTime.now().plusYears(5);

        List<Event> events = eventRepository.getAllForPublicUsers(text, categoryIds, paid, rangeStart, rangeEnd);

        List<ConfirmedRequestShortDto> requestRepoResult = requestsRepository.getCountConfirmedRequestsForAllEvents();
        Map<Long, Integer> countPublishedRequestsForAllEvents = new HashMap<>();
        for (ConfirmedRequestShortDto counts : requestRepoResult) {
            countPublishedRequestsForAllEvents.put(counts.getEventId(), counts.getCount());
        }

        if (onlyAvailable) {
            events = events.stream()
                    .filter(event -> {
                        if (countPublishedRequestsForAllEvents.containsKey(event.getId())) {
                            int countConfirmedRequests = countPublishedRequestsForAllEvents.get(event.getId());

                            if (event.getParticipantLimit() == 0 || event.getParticipantLimit() > countConfirmedRequests) {
                                return true;
                            }
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
        }

        List<EventShortDto> eventShortDtos = EventMapper.toEventShortDtos(events);
        Map<String, Integer> urlToHits = new HashMap<>();
        for (ViewStatsDto viewStatsDto : getViews(events)) {
            urlToHits.put(viewStatsDto.getUri(), viewStatsDto.getHits());
        }

        eventShortDtos.stream()
                .map(eventShortDto -> {
                    String currentUrl = "/events" + eventShortDto.getId();
                    if (urlToHits.containsKey(currentUrl)) {
                        eventShortDto.setViews(urlToHits.get(currentUrl));
                    }

                    if (countPublishedRequestsForAllEvents.containsKey(eventShortDto.getId())) {
                        eventShortDto.setConfirmedRequests(countPublishedRequestsForAllEvents.get(eventShortDto.getId()));
                    }
                    return eventShortDto;
                });

        if (sort.equals("VIEWS")) {
            eventShortDtos.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        } else {
            eventShortDtos.sort(Comparator.comparing(EventShortDto::getEventDate));
        }

        int to = Math.min(from + size, eventShortDtos.size());

        log.info("🟦 для публичного пользователя выдан список событий={}", eventShortDtos.subList(from, to));
        return eventShortDtos.subList(from, to);
    }

    public Event checkInitiatorIdIsLinkedToEventId(long initiatorId, long eventId) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, initiatorId);

        if (event == null) {
            throw new MainExceptionIncompatibleIds("id инициатора: " + initiatorId + " НЕ связан с id события: " + eventId);
        } else {
            return event;
        }
    }

    private void checkNewEventDate(LocalDateTime newEventDate, int constraint) {
        if (newEventDate.isBefore(LocalDateTime.now().plusHours(constraint))) {
            throw new MainExceptionIncorrectDateTime("Field: newEventDate. Error: дата и время на которые намечено событие не может быть раньше, чем через: " + constraint + " час/а от текущего момента. Value: " + newEventDate);
        }
    }

    private List<ViewStatsDto> getViews(List<Event> events) {
        if (events == null || events.isEmpty()) return new ArrayList<>();

        Set<Long> ids = events.stream().map(event -> event.getId()).collect(Collectors.toSet());

        LocalDateTime start;
        if (events.size() == 1) {
            if (events.get(0).getPublishedOn() != null) {
                start = events.get(0).getPublishedOn();
            } else {
                start = LocalDateTime.now();
            }
        } else {
            Optional<Event> event = events.stream()
                    .filter(entity -> entity.getPublishedOn() != null)
                    .sorted(Comparator.comparing(entity -> entity.getPublishedOn()))
                    .findFirst();
            if (event.isPresent()) {
                start = event.get().getPublishedOn();
            } else {
                start = LocalDateTime.now();
            }
        }

        Set<String> points = ids.stream().map(id -> "/events/" + id).collect(Collectors.toSet());

        ResponseEntity<Object> response = statsClient.get(
                start,
                start.plusYears(5),
                points.toArray(new String[]{}),
                true);

        List<ViewStatsDto> stats;
        ObjectMapper mapper = new ObjectMapper();
        try {
            stats = List.of(mapper.readValue(mapper.writeValueAsString(response.getBody()), ViewStatsDto[].class));
        } catch (IOException exception) {
            throw new ClassCastException(exception.getMessage());
        }
        return stats;
    }

    private Event changeEvent(Event event, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest.getAnnotation() != null && !event.getAnnotation().equals(updateEventRequest.getAnnotation())) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null && !event.getCategory().getId().equals(updateEventRequest.getCategory())) {
            event.setCategory(categoryService.checkAndGetEntityById(updateEventRequest.getCategory()));
        }
        if (updateEventRequest.getDescription() != null && !event.getDescription().equals(updateEventRequest.getDescription())) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getLocation() != null && !event.getLocation().equals(LocationMapper.toLocation(updateEventRequest.getLocation()))) {
            event.setLocation(event.getLocation());
        }
        if (updateEventRequest.getPaid() != null && !event.getPaid().equals(updateEventRequest.getPaid())) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null && !event.getParticipantLimit().equals(updateEventRequest.getParticipantLimit())) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null && !event.getRequestModeration().equals(updateEventRequest.getRequestModeration())) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null && !event.getTitle().equals(updateEventRequest.getTitle())) {
            event.setTitle(updateEventRequest.getTitle());
        }
        return event;
    }
}
