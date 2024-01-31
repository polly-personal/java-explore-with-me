package ru.practicum.service.comment;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateAdminCommentDto;
import ru.practicum.dto.comment.UpdateUserCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createForCommentatorByCommentatorIdAndEventId(long commentatorId, NewCommentDto newCommentDto);

    CommentDto updateForAdminByCommentId(UpdateAdminCommentDto updateAdminCommentDto);

    CommentDto updateForCommentatorByCommentatorIdAndCommentId(long commentatorId, UpdateUserCommentDto updateUserCommentDto);

    CommentDto getForAdminByCommentId(long commentId);

    CommentDto getForCommentatorByCommentatorIdAndCommentId(long commentatorId, long commentId);

    CommentDto getForPublicUserByEventIdAndCommentId(long eventId, long commentId);

    List<CommentDto> getAllForPublicUserByEventId(long eventId, int from, int size);

    void deleteForCommentatorByCommentatorIdAndCommentId(long commentatorId, long commentId);
}
