package ru.practicum.controller.users.userid.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.requests.RequestsService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
@RestController
public class UsersPrivateRequestsController {
    private final RequestsService requestsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ParticipationRequestDto postForRequester(@PathVariable("userId") long requesterId, @RequestParam long eventId) {
        log.info("🟫🟫 POST /users/{}/requests?eventId={}", requesterId, eventId);
        log.info("🟤 пришедшие параметры: requesterId={}, eventId={}", requesterId, eventId);
        return requestsService.createForRequester(requesterId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto patchOnCanceledForRequester(@PathVariable("userId") long requesterId, @PathVariable long requestId) {
        log.info("🟫🟫 PATCH /users/{}/requests/{}/cancel", requesterId, requestId);
        log.info("🟤 пришедшие параметры: requesterId={}, requestId={}", requesterId, requestId);
        return requestsService.updateOnCanceledForRequester(requesterId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllForRequester(@PathVariable("userId") long requesterId) {
        log.info("🟫🟫 GET /users/{}/requests", requesterId);
        return requestsService.getAllForRequester(requesterId);
    }
}
