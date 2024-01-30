package ru.practicum.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.request.Request;
import ru.practicum.entity.request.RequestStatus;

import java.util.List;
import java.util.Set;

public interface RequestsRepository extends JpaRepository<Request, Long> {
    Request findByIdAndRequesterId(long eventId, long requesterId);

    List<Request> findAllByRequesterId(long requesterId);

    List<Request> findAllByIdInAndEventId(Set<Long> ids, long eventId);

    List<Request> findAllByEventIdAndEventInitiatorId(long eventId, long initiatorId);

    List<Request> findAllByEventIdAndEventInitiatorIdAndStatus(long eventId, long initiatorId, RequestStatus status);

    Integer countByEventIdAndStatus(long eventId, RequestStatus requestStatus);

    @Query(value = " select r.event_id as eventId, " +
            "count(r.id) as count " +
            "from requests as r " +
            "where r.status = 'CONFIRMED' " +
            "group by r.event_id " +
            "order by r.event_id asc",
            nativeQuery = true)
    List<ConfirmedRequestShortDto> getCountConfirmedRequestsForAllEvents();
}
