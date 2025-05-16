package com.github.osipovvj.webrise_test_task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UserRequest", description = "Запрос на создание и на изменение пользователя.")
public record UserRequest(

        @Schema(description = "Имя пользователя.", example = "User_1")
        @NotNull
        String username,

        @Schema(description = "Email пользователя.", example = "user@example.com")
        @NotNull
        @Email(message = "Неверный формат email.")
        String email
) {
}
