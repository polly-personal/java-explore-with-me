package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.entity.comment.CommentStatus;

import java.time.LocalDateTime;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Builder
@Data
public class CommentDto {
    private Long id;

    private String commentatorName;

    private Long eventId;

    private String text;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime updateDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;

    private CommentStatus status;
}
