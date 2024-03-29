package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.dto.location.LocationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Jacksonized
@Data
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;

    @Positive(message = "поле \"category\" должно быть положительным")
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "поле \"participantLimit\" должно быть положительным или равным нулю")
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    private String title;
}
