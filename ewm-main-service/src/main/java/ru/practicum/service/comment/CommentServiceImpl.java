package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateAdminCommentDto;
import ru.practicum.dto.comment.UpdateUserCommentDto;
import ru.practicum.entity.comment.Comment;
import ru.practicum.entity.comment.CommentStatus;
import ru.practicum.entity.event.Event;
import ru.practicum.entity.event.EventState;
import ru.practicum.exception.MainExceptionIdNotFound;
import ru.practicum.exception.MainExceptionImpossibleToCreateOrUpdateEntity;
import ru.practicum.exception.MainExceptionImpossibleToPublicGetEntity;
import ru.practicum.exception.MainExceptionIncompatibleIds;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.repository.comment.CommentRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CommentDto createForCommentatorByCommentatorIdAndEventId(long commentatorId, NewCommentDto newCommentDto) {
        Event event = eventRepository.findById(newCommentDto.getEvent()).orElseThrow(() -> new MainExceptionIdNotFound("Event with id=" + newCommentDto.getEvent() +
                " was not found"));

        if (!event.getState().equals(EventState.PUBLISHED))
            throw new MainExceptionImpossibleToCreateOrUpdateEntity("событие должно быть в статусе 'PUBLISHED'");

        Comment comment = CommentMapper.toComment(newCommentDto);

        comment.setCommentator(userRepository.findById(commentatorId).orElseThrow(() -> new MainExceptionIdNotFound("User with id=" + commentatorId + " was not found")));
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        comment.setStatus(CommentStatus.PENDING);

        Comment savedComment = commentRepository.save(comment);

        log.info("🟩 создан комментарий={}", savedComment);
        return CommentMapper.toCommentDto(savedComment);
    }

    @Transactional
    @Override
    public CommentDto updateForAdminByCommentId(UpdateAdminCommentDto updateAdminCommentDto) {
        Comment oldComment = checkAndGetEntityById(updateAdminCommentDto.getId());

        if (!oldComment.getStatus().equals(CommentStatus.PENDING))
            throw new MainExceptionImpossibleToCreateOrUpdateEntity("обновить комментарий можно только со статусом 'PENDING'");

        CommentStatus newStatus = updateAdminCommentDto.getStatus();

        Comment updatedComment = oldComment;
        if (newStatus.equals(CommentStatus.CANCELED)) {
            oldComment.setStatus(CommentStatus.CANCELED);

            updatedComment = commentRepository.save(oldComment);

            log.info("🟪 для администратора обновлен комментарий (закрыт) комментарий={}", updatedComment);
        } else if (newStatus.equals(CommentStatus.PUBLISHED)) {
            if (!updateAdminCommentDto.getText().equals(oldComment.getText())) {

                oldComment.setText(updateAdminCommentDto.getText());
                oldComment.setPublishedOn(LocalDateTime.now());
                oldComment.setStatus(CommentStatus.PUBLISHED);

                updatedComment = commentRepository.save(oldComment);

                log.info("🟪 для администратора обновлен (опубликован) комментарий={}", updatedComment);
            } else {
                log.info("🟪 для администратора НЕ обновлен комментарий, тк текст не имеет изменений={}", oldComment);
            }
        }
        return CommentMapper.toCommentDto(updatedComment);
    }

    @Transactional
    @Override
    public CommentDto updateForCommentatorByCommentatorIdAndCommentId(long commentatorId, UpdateUserCommentDto updateUserCommentDto) {
        Comment oldComment = checkCommentatorIdIsLinkedToCommentId(commentatorId, updateUserCommentDto.getId());

        Comment updatedComment = oldComment;
        if (!updateUserCommentDto.getText().equals(oldComment.getText())) {
            oldComment.setText(updateUserCommentDto.getText());
            oldComment.setUpdateDate(LocalDateTime.now());
            oldComment.setStatus(CommentStatus.PENDING);

            updatedComment = commentRepository.save(oldComment);

            log.info("🟪 для комментатора обновлен комментарий={}", updatedComment);
        } else {
            log.info("🟪 для комментатора НЕ обновлен комментарий, тк текст не имеет изменений={}", oldComment);
        }

        return CommentMapper.toCommentDto(updatedComment);
    }

    @Override
    public CommentDto getForAdminByCommentId(long commentId) {
        Comment comment = checkAndGetEntityById(commentId);

        log.info("🟦 для адимистратора выдан комментарий={}", comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto getForCommentatorByCommentatorIdAndCommentId(long commentatorId, long commentId) {
        Comment comment = checkAndGetEntityById(commentId);

        log.info("🟦 для комментатора (зарегестрированного пользователя) выдан комментарий={}", comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto getForPublicUserByEventIdAndCommentId(long eventId, long commentId) {
        Comment comment = commentRepository.findByIdAndEventIdAndStatus(commentId, eventId, CommentStatus.PUBLISHED).orElseThrow(() -> new MainExceptionImpossibleToPublicGetEntity("комментарий должен быть опубликован и id комментария: " + commentId + " должен быть связан с id события: " + eventId));

        log.info("🟦 для публичного пользователя выдан комментарий={}", comment);
        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public List<CommentDto> getAllForPublicUserByEventId(long eventId, int from, int size) {
        Page<Comment> comments = commentRepository.findAllByEventIdAndStatus(eventId, CommentStatus.PUBLISHED,
                PageRequest.of(from, size));

        log.info("🟦 для публичного пользователя выдан список комментариев={}", comments);
        return CommentMapper.toCommentDtos(comments.toList());
    }

    @Override
    public void deleteForCommentatorByCommentatorIdAndCommentId(long commentatorId, long commentId) {
        checkCommentatorIdIsLinkedToCommentId(commentatorId, commentId);

        commentRepository.deleteById(commentId);
        log.info("⬛️ коментатором удален комментарий по id={}", commentId);
    }

    private Comment checkAndGetEntityById(long id) {
        return commentRepository.findById(id).orElseThrow(() -> new MainExceptionIdNotFound("Comment with id=" + id + " was not found"));
    }

    private Comment checkCommentatorIdIsLinkedToCommentId(long commentatorId, long commentId) {
        Comment comment = commentRepository.findByIdAndCommentatorId(commentId, commentatorId);

        if (comment == null) {
            throw new MainExceptionIncompatibleIds("id комментатора: " + commentatorId + " НЕ связан с id " +
                    "комментария: " + commentId);
        } else {
            return comment;
        }
    }
}
