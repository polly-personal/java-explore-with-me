package ru.practicum.controller.users.userid.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateUserCommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
@RestController
public class UsersPrivateCommentsController {
    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommentDto postForCommentatorByCommentatorIdAndEventId(@PathVariable("userId") long commentatorId, @RequestBody @Valid NewCommentDto newCommentDto) {
        log.info("🟫🟫 POST /users/{}/comments/{}", commentatorId);
        log.info("🟤 пришедшие параметры: commentatorId={}, requestCommentDto={}", commentatorId, newCommentDto);
        return commentService.createForCommentatorByCommentatorIdAndEventId(commentatorId, newCommentDto);
    }

    @PatchMapping
    public CommentDto patchForCommentatorByCommentatorIdAndCommentId(@PathVariable(name = "userId") long commentatorId, @RequestBody @Valid UpdateUserCommentDto updateUserCommentDto) {
        log.info("🟫🟫 PATCH /users/{}/comments", commentatorId);
        log.info("🟤 пришедшие параметры: commentatorId={}, requestCommentDto={}", commentatorId, updateUserCommentDto);
        return commentService.updateForCommentatorByCommentatorIdAndCommentId(commentatorId, updateUserCommentDto);
    }

    @GetMapping("/{commentId}")
    public CommentDto getForCommentatorByCommentatorIdAndCommentId(@PathVariable("userId") long commentatorId, @PathVariable long commentId) {
        log.info("🟫🟫 GET /users/{}/comments/{}", commentatorId, commentId);
        return commentService.getForCommentatorByCommentatorIdAndCommentId(commentatorId, commentId);
    }

    @DeleteMapping("/{commentId}")
    public void deleteForCommentatorByCommentatorIdAndCommentId(@PathVariable("userId") long commentatorId, @PathVariable long commentId) {
        log.info("🟫🟫 DELETE /users/{}/comments/{}", commentatorId, commentId);
        commentService.deleteForCommentatorByCommentatorIdAndCommentId(commentatorId, commentId);
    }
}
