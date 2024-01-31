package ru.practicum.controller.comments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping("/comments/{eventId}")
@RestController
public class CommentsPublicController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentDto getForPublicUserByEventIdAndCommentId(@PathVariable long eventId, @PathVariable long commentId) {
        log.info("🟫🟫 GET /comments/{}/{}", eventId, commentId);
        return commentService.getForPublicUserByEventIdAndCommentId(eventId, commentId);
    }

    @GetMapping
    public List<CommentDto> getAllForPublicUserByEventId(@PathVariable long eventId,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        log.info("🟫🟫 GET /comments/{}", eventId);
        return commentService.getAllForPublicUserByEventId(eventId, from, size);
    }
}
