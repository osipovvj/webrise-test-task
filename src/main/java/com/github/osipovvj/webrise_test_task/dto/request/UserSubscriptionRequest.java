package com.github.osipovvj.webrise_test_task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "UserSubscriptionRequest", description = "Запрос на добавление новой подписки пользователю.")
public record UserSubscriptionRequest(
    @Schema(description = "ID сервиса.", example = "12")
    Long subscriptionId,

    @Schema(description = "Стоимость подписки для пользователя.", example = "4.875")
    BigDecimal price
) {
}
