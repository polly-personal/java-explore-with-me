package ru.practicum.dto.user;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Jacksonized
@Builder
@Data
public class NewUserRequest {
    @Size(min = 6, max = 254)
    @Email(message = "некорректное поле \"email\"")
    @NotEmpty(message = "поле \"email\" должно быть заполнено")
    private String email;

    @Size(min = 2, max = 250)
    @NotBlank(message = "поле \"name\" должно быть заполнено")
    private String name;
}
