package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Jacksonized
@Builder
@Data
public class EndpointHitDto {
    private Long id;

    private String app;

    @NotEmpty(message = "поле \"uri\" должно быть заполнено", groups = CreateValidation.class)
    private String uri;

    @NotEmpty(message = "поле \"ip\" должно быть заполнено", groups = CreateValidation.class)
    private String ip;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Past(message = "поле \"timestamp\" должно быть в прошлом", groups = CreateValidation.class)
    @NotNull(message = "поле \"timestamp\" должно быть заполнено", groups = CreateValidation.class)
    private LocalDateTime timestamp;
}
