package ru.practicum.controller.users.userid.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.RequestCommentDto;
import ru.practicum.service.comment.CommentService;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@RestController
public class UsersPrivateCommentsController {
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{eventId}")
    public CommentDto postForCommentatorByCommentatorIdAndEventId(@PathVariable("userId") long commentatorId,
                                                                  @PathVariable long eventId,
                                                                  @RequestBody @Validated RequestCommentDto requestCommentDto) {
        log.info("🟫🟫 POST /users/{}/comments/{}", commentatorId, eventId);
        log.info("🟤 пришедшие параметры: commentatorId={}, eventId={}, requestCommentDto={}", commentatorId, eventId, requestCommentDto);
        return commentService.createForCommentatorByCommentatorIdAndEventId(commentatorId, eventId, requestCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto patchForCommentatorByCommentatorIdAndCommentId(@PathVariable(name = "userId") long commentatorId,
                                                                     @PathVariable long commentId,
                                                                     @RequestBody @Validated RequestCommentDto requestCommentDto) {
        log.info("🟫🟫 PATCH /users/{}/comments/{}", commentatorId, commentId);
        log.info("🟤 пришедшие параметры: commentatorId={}, commentId={}, requestCommentDto={}", commentatorId, commentId, requestCommentDto);
        return commentService.updateForCommentatorByCommentatorIdAndCommentId(commentatorId, commentId, requestCommentDto);
    }

    @GetMapping("/{commentId}")
    public CommentDto getForCommentatorByCommentatorIdAndCommentId(@PathVariable("userId") long commentatorId,
                                                                   @PathVariable long commentId) {
        log.info("🟫🟫 GET /users/{}/events/{}", commentatorId, commentId);
        return commentService.getForCommentatorByCommentatorIdAndCommentId(commentatorId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteForCommentatorByCommentatorIdAndCommentId(@PathVariable("userId") long commentatorId,
                                                                @PathVariable long commentId) {
        log.info("🟫🟫 DELETE /users/{}/events/{}", commentatorId, commentId);
        commentService.deleteForCommentatorByCommentatorIdAndCommentId(commentatorId, commentId);
    }
}
