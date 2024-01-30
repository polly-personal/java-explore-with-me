package ru.practicum.controller.users.userid.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.event.EventService;
import ru.practicum.service.requests.RequestsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@RestController
public class UsersPrivateEventsController {
    private final EventService eventService;
    private final RequestsService requestsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public EventFullDto postForInitiator(@PathVariable long userId, @RequestBody @Validated NewEventDto newEventDto) {
        log.info("🟫🟫 POST /users/{}/events", userId);
        log.info("🟤 пришедшие параметры: userId={}, newEventDto={}", userId, newEventDto);
        return eventService.createForInitiator(userId, newEventDto);
    }

    @PatchMapping("{eventId}")
    public EventFullDto patchForInitiatorByInitiatorIdAndEventId(@PathVariable(name = "userId") long initiatorId, @PathVariable long eventId, @RequestBody @Validated UpdateEventUserRequest updateEventUserRequest) {
        log.info("🟫🟫 GET /users/{}/events/{}", initiatorId, eventId);
        log.info("🟤 пришедшие параметры: initiatorId={}, eventId={}, updateEventUserRequest={}", initiatorId, eventId, updateEventUserRequest);
        return eventService.updateForInitiatorByInitiatorIdAndEventId(initiatorId, eventId, updateEventUserRequest);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventRequestsForInitiatorByInitiatorIdAndEventId(@PathVariable(name = "userId") long initiatorId,
                                                                                                @PathVariable long eventId,
                                                                                                @RequestBody
                                                                                                @Validated EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("🟫🟫 GET /users/{}/events/{}/requests", initiatorId, eventId);
        log.info("🟤 пришедшие параметры: initiatorId={}, eventId={}, eventRequestStatusUpdateRequest={}", initiatorId, eventId, eventRequestStatusUpdateRequest);
        return requestsService.updateEventRequestsForInitiatorByInitiatorIdAndEventId(initiatorId, eventId, eventRequestStatusUpdateRequest);
    }

    @GetMapping("{eventId}")
    public EventFullDto getForInitiatorByInitiatorIdAndEventId(@PathVariable(name = "userId") long initiatorId, @PathVariable long eventId) {
        log.info("🟫🟫 GET /users/{}/events/{}", initiatorId, eventId);
        return eventService.getForInitiatorByInitiatorIdAndEventId(initiatorId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getAllForInitiatorByInitiatorId(@PathVariable(name = "userId") long initiatorId,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                               @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("🟫🟫 GET /users/{}/events", initiatorId);
        return eventService.getAllForInitiatorByInitiatorId(from, size, initiatorId);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getAllEventRequestsForInitiatorByInitiatorIdAndEventId(@PathVariable("userId") long initiatorId, @PathVariable long eventId) {
        log.info("🟫🟫 GET /users/{}/events/{}/requests", initiatorId, eventId);
        return requestsService.getAllEventRequestsForInitiatorByInitiatorIdAndEventId(initiatorId, eventId);
    }
}
