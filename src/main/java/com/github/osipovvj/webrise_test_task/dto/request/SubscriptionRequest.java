package com.github.osipovvj.webrise_test_task.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "SubscriptionRequest", description = "Запрос на создание нового сервиса.")
public record SubscriptionRequest(

    @Schema(description = "Название подписки.", example = "YouTube")
    String subscriptionName,

    @Schema(description = "Название сервиса", example = "YouTube Premium")
    String serviceName,

    @Schema(description = "Адрес сервиса.", example = "https://youtube.com/premium")
    String serviceUrl
) {
}
