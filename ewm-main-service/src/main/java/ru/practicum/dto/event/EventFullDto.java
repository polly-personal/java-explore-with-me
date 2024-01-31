package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.location.LocationDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.entity.event.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Builder
@Data
public class EventFullDto {
    private String annotation;

    private CategoryDto category;

    private int confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private Long id;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;

    private EventState state;

    private String title;

    private int views;

    private List<CommentDto> comments;
}
