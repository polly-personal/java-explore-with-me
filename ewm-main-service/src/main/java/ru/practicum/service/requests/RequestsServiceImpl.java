package ru.practicum.service.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.event.Event;
import ru.practicum.entity.event.EventState;
import ru.practicum.entity.request.Request;
import ru.practicum.entity.request.RequestStatus;
import ru.practicum.entity.user.User;
import ru.practicum.exception.MainExceptionImpossibleToCreateOrUpdateEntity;
import ru.practicum.exception.MainExceptionIncompatibleIds;
import ru.practicum.exception.MainExceptionIdNotFound;
import ru.practicum.mapper.RequestsMapper;
import ru.practicum.repository.request.RequestsRepository;
import ru.practicum.service.event.EventService;
import ru.practicum.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RequestsServiceImpl implements RequestsService {
    private final RequestsRepository requestsRepository;
    private final UserService userService;
    private final EventService eventService;


    @Transactional
    public ParticipationRequestDto createForRequester(long requesterId, long eventId) {
        User user = userService.checkAndGetEntityById(requesterId);
        Event event = eventService.checkAndGetEntityById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new MainExceptionImpossibleToCreateOrUpdateEntity("нельзя участвовать в неопубликованном событии");
        if (event.getInitiator().getId().equals(requesterId))
            throw new MainExceptionIncompatibleIds("инициатор события не может добавить запрос на участие в своём событии");
        if (event.getParticipantLimit() > 0) {
            if (event.getParticipantLimit() <= countConfirmedParticipantsEvent(eventId)) {
                throw new MainExceptionImpossibleToCreateOrUpdateEntity("у события достигнут лимит запросов на участие");
            }
        }

        Request request = Request.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(user)
                .build();
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            request.setStatus(RequestStatus.PENDING);
        } else {
            request.setStatus(RequestStatus.CONFIRMED);
        }

        Request repoRequest = requestsRepository.save(request);

        log.info("🟩 создан запрос={}", repoRequest);
        return RequestsMapper.toParticipationRequestDto(repoRequest);
    }

    @Transactional
    public ParticipationRequestDto updateOnCanceledForRequester(long requesterId, long requestId) {
        userService.checkAndGetEntityById(requesterId);
        Request request = checkRequesterIdIsLinkedToRequestId(requesterId, requestId);

        request.setStatus(RequestStatus.CANCELED);
        Request repoRequest = requestsRepository.save(request);

        log.info("🟪 обновлен запрос_на_участие={}", repoRequest);
        return RequestsMapper.toParticipationRequestDto(repoRequest);
    }

    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestsForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId, EventRequestStatusUpdateRequest request) {
        userService.checkAndGetEntityById(initiatorId);
        eventService.checkAndGetEntityById(eventId);
        Event event = eventService.checkInitiatorIdIsLinkedToEventId(initiatorId, eventId);

        List<Request> requests = requestsRepository.findAllByIdInAndEventId(request.getRequestIds(), eventId);

        EventRequestStatusUpdateRequest.Status newStatus = request.getStatus();
        Integer limit = event.getParticipantLimit();
        AtomicReference<Integer> countConfirmedRequests = new AtomicReference<>(countConfirmedParticipantsEvent(eventId));

        if (limit > 0 && limit <= countConfirmedRequests.get()) {
            throw new MainExceptionImpossibleToCreateOrUpdateEntity("у события достигнут лимит запросов на участие");
        }

        if (limit != 0 || event.getRequestModeration()) {
            requests.stream().forEach(req -> {

                if (!req.getStatus().equals(RequestStatus.PENDING))
                    throw new MainExceptionImpossibleToCreateOrUpdateEntity("статус можно изменить только у заявок, находящихся в состоянии ожидания");

                if (limit > countConfirmedRequests.get()) {
                    if (newStatus.equals(EventRequestStatusUpdateRequest.Status.CONFIRMED))
                        req.setStatus(RequestStatus.CONFIRMED);
                    if (newStatus.equals(EventRequestStatusUpdateRequest.Status.REJECTED))
                        req.setStatus(RequestStatus.REJECTED);
                } else {
                    req.setStatus(RequestStatus.REJECTED);
                }

                if (req.getStatus().equals(RequestStatus.CONFIRMED)) {
                    countConfirmedRequests.getAndSet(countConfirmedRequests.get() + 1);
                }
            });

            requestsRepository.saveAll(requests);
        }

        List<Request> confirmedRequests = requestsRepository.findAllByEventIdAndEventInitiatorIdAndStatus(eventId, initiatorId, RequestStatus.CONFIRMED);
        List<Request> rejectedRequests = requestsRepository.findAllByEventIdAndEventInitiatorIdAndStatus(eventId, initiatorId, RequestStatus.REJECTED);

        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(RequestsMapper.toParticipationRequestDtos(confirmedRequests))
                .rejectedRequests(RequestsMapper.toParticipationRequestDtos(rejectedRequests))
                .build();

        log.info("🟪 обновлен список запросов_на_участие={}", result);
        return result;
    }

    public Request checkAndGetEntityById(long id) {
        return requestsRepository.findById(id).orElseThrow(() -> new MainExceptionIdNotFound("Request with id=" + id +
                " was not found"));
    }

    public List<ParticipationRequestDto> getAllForRequester(long requesterId) {
        userService.checkAndGetEntityById(requesterId);
        List<Request> requests = requestsRepository.findAllByRequesterId(requesterId);

        log.info("🟦 выдан список запросов_на_участие_в_СОБЫТИЯХ={}", requests);
        return RequestsMapper.toParticipationRequestDtos(requests);
    }

    public List<ParticipationRequestDto> getAllEventRequestsForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId) {
        userService.checkAndGetEntityById(initiatorId);
        eventService.checkAndGetEntityById(eventId);
        List<Request> requests = requestsRepository.findAllByEventIdAndEventInitiatorId(eventId, initiatorId);

        log.info("🟦 выдан список запросов_на_участие_в_СОБЫТИИ={}", requests);
        return RequestsMapper.toParticipationRequestDtos(requests);
    }

    public Integer countConfirmedParticipantsEvent(long eventId) {
        return requestsRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    private Request checkRequesterIdIsLinkedToRequestId(long requesterId, long requestId) {
        Request request = requestsRepository.findByIdAndRequesterId(requestId, requesterId);

        if (request == null) {
            throw new MainExceptionIncompatibleIds("id запрашивающего: " + requesterId + " НЕ связан с " +
                    "id запроса: " + requestId);
        } else {
            return request;
        }
    }
}
