package ru.practicum.repository.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.entity.comment.Comment;
import ru.practicum.entity.comment.CommentStatus;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIdAndCommentatorId(long commentId, long commentatorId);

    Optional<Comment> findByIdAndEventIdAndStatus(long commentId, long eventId, CommentStatus commentStatus);

    Page<Comment> findAllByEventIdAndStatus(long eventId, CommentStatus commentStatus, PageRequest pageRequest);

    List<Comment> findAllByEventIdAndStatus(long eventId, CommentStatus commentStatus);

    @Query(value = " select c.event_id as eventId, " +
            "count(c.id) as count " +
            "from comments as c " +
            "where c.status = 'PUBLISHED' " +
            "group by c.event_id " +
            "order by c.event_id asc",
            nativeQuery = true)
    List<CountOfEventComments> getCountPublishedCommentsForAllEvents();
}
