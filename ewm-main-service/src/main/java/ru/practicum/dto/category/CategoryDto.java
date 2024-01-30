package ru.practicum.dto.category;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@Data
public class CategoryDto {
    private Long id;

    @Size(min = 1, max = 50)
    @NotBlank(message = "поле \"name\" должно быть заполнено")
    private String name;
}
