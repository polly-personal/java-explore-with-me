package ru.practicum.service.requests;

import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.entity.request.Request;

import java.util.List;

public interface RequestsService {
    ParticipationRequestDto createForRequester(long requesterId, long eventId);

    ParticipationRequestDto updateOnCanceledForRequester(long requesterId, long requestId);

    EventRequestStatusUpdateResult updateEventRequestsForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    Request checkAndGetEntityById(long id);

    List<ParticipationRequestDto> getAllForRequester(long requesterId);

    List<ParticipationRequestDto> getAllEventRequestsForInitiatorByInitiatorIdAndEventId(long initiatorId, long eventId);

    Integer countConfirmedParticipantsEvent(long eventId);
}
