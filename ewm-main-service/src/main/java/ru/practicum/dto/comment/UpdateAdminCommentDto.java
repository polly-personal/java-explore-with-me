package ru.practicum.dto.comment;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.entity.comment.CommentStatus;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@Data
public class UpdateAdminCommentDto {
    @NotNull(message = "поле \"id\" должно быть заполнено")
    private Long id;

    @Size(min = 1, max = 7000)
    private String text;

    @NotNull(message = "поле \"status\" должно быть заполнено")
    private CommentStatus status;
}
