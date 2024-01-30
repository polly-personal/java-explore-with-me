package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.entity.comment.CommentStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@Data
public class RequestCommentDto {

    @Size(min = 1, max = 7000)
    @NotBlank(message = "поле \"text\" должно быть заполнено")
    private String text;

    @NotNull(message = "поле \"status\" должно быть заполнено")
    private CommentStatus status;
}
