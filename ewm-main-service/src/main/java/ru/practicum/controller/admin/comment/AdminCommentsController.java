package ru.practicum.controller.admin.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateAdminCommentDto;
import ru.practicum.service.comment.CommentService;

import javax.validation.Valid;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
@RestController
public class AdminCommentsController {
    private final CommentService commentService;

    @PatchMapping
    public CommentDto patchForAdminByCommentId(@RequestBody @Valid UpdateAdminCommentDto updateAdminCommentDto) {
        log.info("🟫🟫 POST /admin/comments");
        log.info("🟤 пришедшие параметры: updateAdminCommentDto={}", updateAdminCommentDto);
        return commentService.updateForAdminByCommentId(updateAdminCommentDto);
    }

    @GetMapping("/{commentId}")
    public CommentDto getForAdminByCommentId(@PathVariable long commentId) {
        log.info("🟫🟫 GET /admin/comments/{}", commentId);
        return commentService.getForAdminByCommentId(commentId);
    }
}
