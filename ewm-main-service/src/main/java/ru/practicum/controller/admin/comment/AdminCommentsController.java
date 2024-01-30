package ru.practicum.controller.admin.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.RequestCommentDto;
import ru.practicum.service.comment.CommentService;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/comments/{commentId}")
@RestController
public class AdminCommentsController {
    private final CommentService commentService;

    @PatchMapping
    public CommentDto patchForAdminByCommentId(@PathVariable long commentId,
                                               @RequestBody @Validated RequestCommentDto requestCommentDto) {
        log.info("🟫🟫 POST /admin/comments/{}", commentId);
        log.info("🟤 пришедшие параметры: commentId={}, requestCommentDto={}", commentId, requestCommentDto);
        return commentService.updateForAdminByCommentId(commentId, requestCommentDto);
    }

    @GetMapping
    public CommentDto getForAdminByCommentId(@PathVariable long commentId) {
        log.info("🟫🟫 GET /admin/comments/{}", commentId);
        return commentService.getForAdminByCommentId(commentId);
    }
}
