package ru.practicum.controller.users.userid.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.PostValidation;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.event.EventService;
import ru.practicum.service.requests.RequestsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
@RestController
public class UsersPrivateEventsController {
    private final EventService eventService;
    private final RequestsService requestsService;

    @PostMapping
    public EventFullDto postForInitiator(@PathVariable long userId,
                                         @RequestBody /*@Validated(PostValidation.class)*/ @Valid NewEventDto newEventDto) {
        log.info("🟫🟫 POST /users/{}/events", userId);
        return eventService.createForInitiator(userId, newEventDto);
    }

    @PatchMapping("{eventId}")
    public EventFullDto patchEventForInitiatorByInitiatorIdAndEventId(@PathVariable(name = "userId") long initiatorId,
                                                                      @PathVariable long eventId,
                                                                      @RequestBody @Validated(PostValidation.class) UpdateEventUserRequest updateEventUserRequest) {
        log.info("🟫🟫 GET /users/{}/events/{}", initiatorId, eventId);
        return eventService.updateEventForInitiatorByInitiatorIdAndEventId(initiatorId, eventId, updateEventUserRequest);
    }

    @PatchMapping("{eventId}/requests")
    public EventRequestStatusUpdateResult patchEventRequestsForInitiatorByInitiatorIdAndEventId(@PathVariable(name = "userId") long initiatorId,
                                                                                                @PathVariable long eventId,
                                                                                                @RequestBody @Validated(PostValidation.class) EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("🟫🟫 GET /users/{}/events/{}/requests", initiatorId, eventId);
        return requestsService.updateEventRequestsForInitiatorByInitiatorIdAndEventId(initiatorId, eventId,
                eventRequestStatusUpdateRequest);
    }

    @GetMapping("{eventId}")
    public EventFullDto getForInitiatorByInitiatorIdAndEventId(@PathVariable(name = "userId") long initiatorId,
                                                               @PathVariable long eventId) {
        log.info("🟫🟫 GET /users/{}/events/{}", initiatorId, eventId);
        return eventService.getForInitiatorByInitiatorIdAndEventId(initiatorId, eventId);
    }

    @GetMapping
    public List<EventShortDto> getAllForInitiatorByInitiatorId(@PathVariable(name = "userId") long initiatorId,
                                                               @RequestParam(required = false, defaultValue = "0") int from,
                                                               @RequestParam(required = false, defaultValue = "10") int size) {
        log.info("🟫🟫 GET /users/{}/events", initiatorId);
        return eventService.getAllForInitiatorByInitiatorId(from, size, initiatorId);
    }

    @GetMapping("{eventId}/requests")
    public List<ParticipationRequestDto> getAllEventRequestsForInitiatorByInitiatorIdAndEventId(@PathVariable("userId") long initiatorId, @PathVariable long eventId) {
        log.info("🟫🟫 GET /users/{}/events/{}/requests", initiatorId, eventId);
        return requestsService.getAllEventRequestsForInitiatorByInitiatorIdAndEventId(initiatorId, eventId);
    }
}
