package ru.practicum.dto.category;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.dto.PatchValidation;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@Data
public class CategoryDto {
    private Long id;

    @Size(min = 1, max = 50)
    @NotEmpty(message = "поле \"name\" должно быть заполнено", groups = PatchValidation.class)
    private String name;
}