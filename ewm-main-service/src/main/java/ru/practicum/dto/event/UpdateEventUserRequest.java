package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.dto.PatchValidation;
import ru.practicum.dto.location.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Jacksonized
@Builder
@Data
public class UpdateEventUserRequest {
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    /*@Future(message = "поле \"eventDate\" должно быть в будущем", groups = PostValidation.class) проверка в github */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "поле \"participantLimit\" должно быть положительным или равным нулю", groups =
            PatchValidation.class)
    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
