package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import ru.practicum.service.location.LocationService;
import ru.practicum.service.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;

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
        event.setLocation(locationService.getByLatAndLonOrCreateEntity(event.getLocation()));
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(userService.checkAndGetEntityById(userId));
        event.setState(EventState.PENDING);

        Event resultEvent = eventRepository.save(event);

        log.info("🟩 создано событие: " + resultEvent);
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
            if (updateEventAdminRequest.getStateAction().equals("PUBLISH_EVENT")) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }
            if (updateEventAdminRequest.getStateAction().equals("REJECT_EVENT")) {
                event.setState(EventState.CANCELED);
            }
        }

        if (updateEventAdminRequest.getEventDate() != null && !event.getEventDate().equals(updateEventAdminRequest.getEventDate())) {
            checkNewEventDate(updateEventAdminRequest.getEventDate(), 1);
            event.setEventDate(updateEventAdminRequest.getEventDate());
        }

        if (updateEventAdminRequest.getAnnotation() != null && !event.getAnnotation().equals(updateEventAdminRequest.getAnnotation())) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getCategory() != null && !event.getCategory().getId().equals(updateEventAdminRequest.getCategory())) {
            event.setCategory(categoryService.checkAndGetEntityById(updateEventAdminRequest.getCategory()));
        }
        if (updateEventAdminRequest.getDescription() != null && !event.getDescription().equals(updateEventAdminRequest.getDescription())) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getLocation() != null && !event.getLocation().equals(LocationMapper.toLocation(updateEventAdminRequest.getLocation()))) {
            event.setLocation(locationService.getByLatAndLonOrCreateEntity(LocationMapper.toLocation(updateEventAdminRequest.getLocation())));
        }
        if (updateEventAdminRequest.getPaid() != null && !event.getPaid().equals(updateEventAdminRequest.getPaid())) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null && !event.getParticipantLimit().equals(updateEventAdminRequest.getParticipantLimit())) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getRequestModeration() != null && !event.getRequestModeration().equals(updateEventAdminRequest.getRequestModeration())) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }
        if (updateEventAdminRequest.getTitle() != null && !event.getTitle().equals(updateEventAdminRequest.getTitle())) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        Event repoResult = eventRepository.save(event);

        log.info("🟪 обновлено событие для администратора: " + repoResult);
        return EventMapper.toEventFullDto(repoResult);
    }

    @Transactional
    public EventFullDto updateEventForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        checkAndGetEntityById(eventId);
        userService.checkAndGetEntityById(initiatorId);

        Event event = checkInitiatorIdIsLinkedToEventId(initiatorId, eventId);

        EventState currentEventState = event.getState();
        if (currentEventState.equals(EventState.PUBLISHED)) {
            throw new MainExceptionImpossibleToCreateOrUpdateEntity("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequest.getStateAction() != null) {
            if (event.getState().equals(EventState.PENDING) && updateEventUserRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState(EventState.CANCELED);
            }
            if (event.getState().equals(EventState.CANCELED) && updateEventUserRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState(EventState.PENDING);
            }
        }

        if (updateEventUserRequest.getEventDate() != null && !event.getEventDate().equals(updateEventUserRequest.getEventDate())) {
            checkNewEventDate(updateEventUserRequest.getEventDate(), 2);
            event.setEventDate(updateEventUserRequest.getEventDate());
        }

        if (updateEventUserRequest.getAnnotation() != null && !event.getAnnotation().equals(updateEventUserRequest.getAnnotation())) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }
        if (updateEventUserRequest.getCategory() != null && !event.getCategory().getId().equals(updateEventUserRequest.getCategory())) {
            event.setCategory(categoryService.checkAndGetEntityById(updateEventUserRequest.getCategory()));
        }
        if (updateEventUserRequest.getDescription() != null && !event.getDescription().equals(updateEventUserRequest.getDescription())) {
            event.setDescription(updateEventUserRequest.getDescription());
        }
        if (updateEventUserRequest.getLocation() != null && !event.getLocation().equals(LocationMapper.toLocation(updateEventUserRequest.getLocation()))) {
            event.setLocation(locationService.getByLatAndLonOrCreateEntity(LocationMapper.toLocation(updateEventUserRequest.getLocation())));
        }
        if (updateEventUserRequest.getPaid() != null && !event.getPaid().equals(updateEventUserRequest.getPaid())) {
            event.setPaid(updateEventUserRequest.getPaid());
        }
        if (updateEventUserRequest.getParticipantLimit() != null && !event.getParticipantLimit().equals(updateEventUserRequest.getParticipantLimit())) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }
        if (updateEventUserRequest.getRequestModeration() != null && !event.getRequestModeration().equals(updateEventUserRequest.getRequestModeration())) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }
        if (updateEventUserRequest.getTitle() != null && !event.getTitle().equals(updateEventUserRequest.getTitle())) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        Event repoResult = eventRepository.save(event);

        log.info("🟪 обновлено событие для инициатора: " + repoResult);
        return EventMapper.toEventFullDto(repoResult);
    }

    public Event checkAndGetEntityById(long id) {
        return eventRepository.findById(id).orElseThrow(() -> new MainExceptionIdNotFound("Event with id=" + id + " was not found"));
    }

    public EventFullDto getForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId) {
        checkAndGetEntityById(eventId);
        userService.checkAndGetEntityById(initiatorId);

        Event event = checkInitiatorIdIsLinkedToEventId(initiatorId, eventId);

        log.info("🟦 выдано событие: " + event);
        return EventMapper.toEventFullDto(event);
    }

    public EventFullDto getForPublicUsersById(long id) {
        Event event = checkAndGetEntityById(id);

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new MainExceptionImpossibleToPublicGetEntity("событие должно быть опубликовано");

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(requestsRepository.countByEventIdAndStatus(id, RequestStatus.CONFIRMED));
        eventFullDto.setViews(getViews(id));

        log.info("🟦 выдано событие: " + eventFullDto);
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

        eventFullDtos.stream().forEach(eventShortDto -> {
            eventShortDto.setViews(getViews(eventShortDto.getId()));

            if (countPublishedRequestsForAllEvents.containsKey(eventShortDto.getId())) {
                eventShortDto.setConfirmedRequests(countPublishedRequestsForAllEvents.get(eventShortDto.getId()));
            }
        });

        log.info("🟦 для администратора выдан список событий: " + eventFullDtos);
        return eventFullDtos;
    }

    public List<EventShortDto> getAllForInitiatorByInitiatorId(int from, int size, long initiatorId) {
        userService.checkAndGetEntityById(initiatorId);

        PageRequest pageRequest = PageRequest.of(from, size);
        Page<Event> events = eventRepository.findAllByInitiatorId(initiatorId, pageRequest);

        log.info("🟦 для инициатора событий с id: " + initiatorId + " выдан список событий: " + events.toList());
        return EventMapper.toEventShortDtos(events.toList());
    }

    public List<EventShortDto> getAllForPublicUsers(String text, List<Long> categoryIds, Boolean paid,
                                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable, String sort, int from, int size) {
        if (categoryIds == null) categoryIds = new ArrayList<>();
        if (rangeStart == null) rangeStart = LocalDateTime.now();
        if (rangeEnd == null) rangeEnd = LocalDateTime.now().plusYears(5);

        Sort repoSort;
        if (sort.equals("VIEWS")) {
            repoSort = Sort.by(Sort.Direction.DESC, "views");
        } else {
            repoSort = Sort.by(Sort.Direction.DESC, "eventDate");
        }
        PageRequest pageRequest = PageRequest.of(from, size, repoSort);
        /*PageRequest pageRequest = PageRequest.of(from > 0 ? from / size : 0, size, repoSort);*/
        Page<Event> eventPages = eventRepository.getAllForPublicUsers(text, categoryIds, paid, rangeStart, rangeEnd, pageRequest);

        List<ConfirmedRequestShortDto> requestRepoResult = requestsRepository.getCountConfirmedRequestsForAllEvents();
        Map<Long, Integer> countPublishedRequestsForAllEvents = new HashMap<>();
        for (ConfirmedRequestShortDto counts : requestRepoResult) {
            countPublishedRequestsForAllEvents.put(counts.getEventId(), counts.getCount());
        }

        List<Event> events = eventPages.toList();

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

        eventShortDtos.stream().forEach(eventShortDto -> {
            eventShortDto.setViews(getViews(eventShortDto.getId()));

            if (countPublishedRequestsForAllEvents.containsKey(eventShortDto.getId())) {
                eventShortDto.setConfirmedRequests(countPublishedRequestsForAllEvents.get(eventShortDto.getId()));
            }
        });

        log.info("🟦 для публичного пользователя выдан список событий: " + eventShortDtos);
        return eventShortDtos;
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

    private int getViews(long eventId) {
        String[] points = {"/events/" + eventId};
        List<ViewStatsDto> response = statsClient.get(
                eventId,
                LocalDateTime.now().minusYears(5),
                LocalDateTime.now().plusYears(5),
                points,
                true);
        return response.size();
    }
}
