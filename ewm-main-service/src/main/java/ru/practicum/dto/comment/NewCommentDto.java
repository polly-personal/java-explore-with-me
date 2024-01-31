package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@Data
public class NewCommentDto {
    @NotNull(message = "поле \"eventId\" должно быть заполнено")
    private Long event;

    @Size(min = 1, max = 7000)
    @NotBlank(message = "поле \"text\" должно быть заполнено")
    private String text;
}
