package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@Data
public class NewCompilationDto {
    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50)
    @NotBlank(message = "поле \"title\" должно быть заполнено")
    private String title;
}
