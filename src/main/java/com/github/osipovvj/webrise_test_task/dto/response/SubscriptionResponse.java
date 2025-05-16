package com.github.osipovvj.webrise_test_task.dto.response;

import com.github.osipovvj.webrise_test_task.entity.Subscription;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "SubscriptionResponse", description = "Ответ сервера с информацией о сервисе подписки.")
public record SubscriptionResponse(
        @Schema(description = "ID сервиса.", example = "12")
        Long id,

        @Schema(description = "Название подписки.", example = "YouTube")
        String subscriptionName,

        @Schema(description = "Название сервиса", example = "YouTube Premium")
        String serviceName,

        @Schema(description = "Адрес сервиса.", example = "https://youtube.com/premium")
        String serviceUrl,

        @Schema(description = "Дата создания подписки.", example = "2021-02-22T09:49:19.275039200")
        LocalDateTime createdAt
) {
        public static SubscriptionResponse toResponse(final Subscription subscription) {
                return new SubscriptionResponse(
                        subscription.getId(),
                        subscription.getSubscriptionName(),
                        subscription.getServiceName(),
                        subscription.getServiceUrl(),
                        subscription.getCreatedAt()
                );
        }
}
