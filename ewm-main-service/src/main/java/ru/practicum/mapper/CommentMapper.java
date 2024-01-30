package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.RequestCommentDto;
import ru.practicum.entity.comment.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class CommentMapper {
    public Comment toComment(RequestCommentDto requestCommentDto) {
        Comment comment = Comment.builder()
                .text(requestCommentDto.getText())
                .status(requestCommentDto.getStatus())
                .build();

        log.info("🔀\nDTO={} сконвертирован в \nJPA-сущность={}", requestCommentDto, comment);
        return comment;
    }

    public CommentDto toCommentDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .commentatorName(comment.getCommentator().getName())
                .eventId(comment.getEvent().getId())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .updateDate(comment.getUpdateDate())
                .publishedOn(comment.getPublishedOn())
                .status(comment.getStatus())
                .build();

        log.info("🔀 \nJPA-сущность={} сконвертирована в \nDTO={}", comment, commentDto);
        return commentDto;
    }

    public List<CommentDto> toCommentDtos(List<Comment> comments) {
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());

        log.info("🔀 \nсписок JPA-сущностей={} сконвертирован в \nсписок DTO={}", comments, commentDtos);
        return commentDtos;
    }
}
