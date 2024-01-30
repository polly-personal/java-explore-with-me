package ru.practicum.repository.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.comment.Comment;
import ru.practicum.entity.comment.CommentStatus;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findByIdAndCommentatorId(long commentId, long commentatorId);

    Optional<Comment> findByIdAndEventIdAndStatus(long commentId, long eventId, CommentStatus commentStatus);

    Page<Comment> findAllByEventIdAndStatus(long eventId, CommentStatus commentStatus, PageRequest pageRequest);
}
