package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.dto.location.LocationDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static ru.practicum.constant.MainConstant.DATE_TIME_PATTERN;

@Jacksonized
@Builder
@Data
public class NewEventDto {
    @Size(min = 20, max = 2000)
    @NotBlank(message = "поле \"annotation\" должно быть заполнено")
    private String annotation;

    @Positive(message = "поле \"category\" должно быть положительным")
    @NotNull(message = "поле \"category\" должно быть заполнено")
    private Long category;

    @Size(min = 20, max = 7000)
    @NotBlank(message = "поле \"description\" должно быть заполнено"/**/)
    private String description;

    @Future(message = "поле \"eventDate\" должно быть в будущем")
    @NotNull(message = "поле \"eventDate\" должно быть заполнено")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_PATTERN)
    private LocalDateTime eventDate;

    @Valid
    @NotNull(message = "поле \"location\" должно быть заполнено")
    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(message = "поле \"participantLimit\" должно быть положительным или равным нулю")
    private Integer participantLimit;

    private Boolean requestModeration;

    @Size(min = 3, max = 120)
    @NotBlank(message = "поле \"title\" должно быть заполнено")
    private String title;
}
