package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class NewCompilationDto { // In
    @NotNull(message = "поле \"events\" должно быть заполнено")
    private List<Long> events;

    @NotNull(message = "поле \"pinned\" должно быть заполнено")
    private Boolean pinned;

    @Size(min = 1, max = 50)
    @NotBlank(message = "поле \"title\" должно быть заполнено")
    private String title;
}
