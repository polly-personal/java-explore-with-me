package ru.practicum.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.dto.PostValidation;

import javax.validation.constraints.NotEmpty;

@Jacksonized
@Builder
@Data
public class NewUserRequest {
    /*@Email(message = "некорректное поле \"email\"", groups = CreateValidation.class)*/
    @NotEmpty(message = "поле \"email\" должно быть заполнено", groups = PostValidation.class)
    private String email;

    @NotEmpty(message = "поле \"name\" должно быть заполнено", groups = PostValidation.class)
    private String name;
}
