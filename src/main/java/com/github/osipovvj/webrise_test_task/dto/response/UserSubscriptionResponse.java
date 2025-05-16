package com.github.osipovvj.webrise_test_task.dto.response;

import com.github.osipovvj.webrise_test_task.entity.UserSubscription;
import com.github.osipovvj.webrise_test_task.enums.SubscriptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(name = "UserSubscriptionResponse", description = "Ответ сервера с информацией о подписке пользователя.")
public record UserSubscriptionResponse(
        @Schema(description = "ID подписки.", example = "12")
        Long id,

        @Schema(description = "Стоимость подписки для пользователя.", example = "4.875")
        BigDecimal price,

        @Schema(description = "Статус подписки.", example = "ACTIVE")
        SubscriptionStatus status,

        @Schema(description = "Дата подписки пользователя на сервис.", example = "2025-09-01T09:49:19.275039200")
        LocalDateTime subscribedAt,

        @Schema(description = "Дата измениния состояния.", example = "2025-09-01T09:49:19.275039200")
        LocalDateTime updatedAt,

        @Schema(description = "Подробная информация о сервисе.")
        SubscriptionResponse subscription
) {

        public static UserSubscriptionResponse toResponse(final UserSubscription userSubscription) {
                return new UserSubscriptionResponse(
                        userSubscription.getId(),
                        userSubscription.getPrice(),
                        userSubscription.getStatus(),
                        userSubscription.getSubscribedAt(),
                        userSubscription.getUpdatedAt(),
                        SubscriptionResponse.toResponse(userSubscription.getSubscription())
                );
        }
}
