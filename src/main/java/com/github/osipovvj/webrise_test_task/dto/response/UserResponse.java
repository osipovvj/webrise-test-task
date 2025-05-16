package com.github.osipovvj.webrise_test_task.dto.response;

import com.github.osipovvj.webrise_test_task.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "UserResponse", description = "Ответ сервера с данными о пользователе.")
public record UserResponse(

        @Schema(description = "ID пользователя.", example = "123")
        Long id,

        @Schema(description = "Имя пользователя.", example = "User_1")
        String username,

        @Schema(description = "Email пользователя.", example = "user@example.com")
        String email,

        @Schema(description = "Дата регистрации пользователя.", example = "2019-02-22T09:49:19.275039200")
        LocalDateTime createdAt,

        @Schema(description = "Дата изменения данных пользователя.", example = "2025-09-01T09:49:19.275039200")
        LocalDateTime updatedAt
) {

    public static UserResponse toResponse(final User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
