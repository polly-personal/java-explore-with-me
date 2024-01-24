package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.PatchValidation;

import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
public class UpdateCompilationRequest {
    private List<Long> events;

    private Boolean pinned;

    @Size(min = 1, max = 50, groups = PatchValidation.class)
    private String title;
}
