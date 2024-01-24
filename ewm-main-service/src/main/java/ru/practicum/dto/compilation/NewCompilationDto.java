package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.PostValidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class NewCompilationDto { // In
    @NotNull(message = "поле \"events\" должно быть заполнено", groups = PostValidation.class)
    private List<Long> events;

    @NotNull(message = "поле \"pinned\" должно быть заполнено", groups = PostValidation.class)
    private Boolean pinned;

    @Size(min = 1, max = 50, groups = PostValidation.class)
    @NotEmpty(message = "поле \"title\" должно быть заполнено", groups = PostValidation.class)
    private String title;
}
