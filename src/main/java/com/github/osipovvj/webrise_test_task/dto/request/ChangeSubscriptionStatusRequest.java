package com.github.osipovvj.webrise_test_task.dto.request;

import com.github.osipovvj.webrise_test_task.enums.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ChangeSubscriptionStatusRequest", description = "Запрос на изменение статуса подписки.")
public record ChangeSubscriptionStatusRequest(
        @Schema(description = "Статус подписки.", example = "INACTIVE")
        SubscriptionStatus subscriptionStatus
) {
}
