package ru.practicum.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Jacksonized
@Builder
@Data
public class NewUserRequest {
    @Email(message = "некорректное поле \"email\"")
    @NotEmpty(message = "поле \"email\" должно быть заполнено")
    private String email;

    @NotEmpty(message = "поле \"name\" должно быть заполнено")
    private String name;
}
