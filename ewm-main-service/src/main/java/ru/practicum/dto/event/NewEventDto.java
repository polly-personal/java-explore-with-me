package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.validation.annotation.Validated;
import ru.practicum.dto.PostValidation;
import ru.practicum.dto.location.LocationDto;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Validated
@Jacksonized
@Builder
@Data
public class NewEventDto {
    @Size(min = 20, max = 2000)
    @NotEmpty(message = "поле \"annotation\" должно быть заполнено", groups = PostValidation.class)
    private String annotation;

    @Positive(message = "поле \"category\" должно быть положительным", groups = PostValidation.class)
    @NotNull(message = "поле \"category\" должно быть заполнено", groups = PostValidation.class)
    private Long category;

    @Size(min = 20, max = 7000)
    @NotEmpty(message = "поле \"description\" должно быть заполнено", groups = PostValidation.class)
    private String description;

    @Future(message = "поле \"eventDate\" должно быть в будущем", groups = PostValidation.class)
    @NotNull(message = "поле \"eventDate\" должно быть заполнено", groups = PostValidation.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @NotNull(message = "поле \"location\" должно быть заполнено", groups = PostValidation.class)
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "поле \"participantLimit\" должно быть положительным или равным нулю", groups = PostValidation.class)
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    @NotNull(message = "поле \"title\" должно быть заполнено", groups = PostValidation.class)
    private String title;
}
