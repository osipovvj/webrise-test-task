package com.github.osipovvj.webrise_test_task.exception.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "ProblemDetailResponse", description = "Ответ сервера в случае возникновения ошибки.")
public record ProblemDetailResponse(
    @Schema(description = "Время возникновения ошибки.", example = "2025-05-15T21:00:00Z")
    LocalDateTime timestamp,

    @Schema(description = "Идентификатор ошибки.", example = "/errors/...")
    String type,

    @Schema(description = "Название ошибки.", example = "... Error")
    String title,

    @Schema(description = "HTTP статус-код.", example = "40..")
    int status,

    @Schema(description = "Описание ошибки.", example = "Пользователь с ...")
    String detail,

    String instance,
    @Schema(description = "Список возникших ошибок.")
    List<FieldErrorDetail> errors
) {

    @Schema(name = "FieldErrorDetail", description = "Детали ошибок по конкретному полю.")
    public record FieldErrorDetail(
            @Schema(description = "Наименование свойства в котором возникла ошибка.", example = "email/name/..")
            String field,

            @Schema(description = "Сообщение об ошибке.", example = "Некорректные данные.")
            String message
    ) {
    }
}
