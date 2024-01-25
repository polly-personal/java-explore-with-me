package ru.practicum.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long initiatorId, PageRequest pageRequest);

    Event findByIdAndInitiatorId(long eventId, long initiatorId);

    @Query(value = "select * " +
            "from events e " +
            "where (cast(e.event_date as date)) between :start and :end " +
            "and (e.initiator_id in :users or :users is null) " +
            "and (e.state in :states or :states is null) " +
            "and (e.category_id in :categories or :categories is null) ",
            nativeQuery = true)
    Page<Event> getAllForAdmin(List<Long> users, List<String> states, List<Long> categories, LocalDateTime start, LocalDateTime end, PageRequest pageRequest);

    @Query(value = "select * " +
            "from events e " +
            "where (e.state = 'PUBLISHED') " +
            "and (lower(e.annotation) like lower(concat('%', :text, '%')) or lower(e.description) like lower(concat" +
            "('%', :text, '%'))) " +
            "and (e.category_id in :categoryIds or :categoryIds is null) " +
            "and (e.is_paid = :paid or :paid is null) " +
            "and (cast(e.event_date as date)) between :rangeStart and :rangeEnd ",
            nativeQuery = true
    )
    List<Event> getAllForPublicUsers(String text, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd);
}
