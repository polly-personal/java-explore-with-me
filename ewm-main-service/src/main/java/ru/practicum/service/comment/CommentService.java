package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.RequestCommentDto;
import ru.practicum.entity.comment.Comment;

import java.util.List;

public interface CommentService {
    CommentDto createForCommentatorByCommentatorIdAndEventId(long commentatorId, long eventId, RequestCommentDto requestCommentDto);

    CommentDto updateForAdminByCommentId(long commentId, RequestCommentDto requestCommentDto);

    CommentDto updateForCommentatorByCommentatorIdAndCommentId(long commentatorId, long commentId, RequestCommentDto requestCommentDto);

    Comment checkAndGetEntityById(long id);

    CommentDto getForAdminByCommentId(long commentId);

    CommentDto getForCommentatorByCommentatorIdAndCommentId(long commentatorId, long commentId);

    CommentDto getForPublicUserByEventIdAndCommentId(long eventId, long commentId);

    List<CommentDto> getAllForPublicUserByEventId(long eventId, int from, int size);

    void deleteForCommentatorByCommentatorIdAndCommentId(long commentatorId, long commentId);

    Comment checkCommentatorIdIsLinkedToCommentId(long commentatorId, long commentId);
}
