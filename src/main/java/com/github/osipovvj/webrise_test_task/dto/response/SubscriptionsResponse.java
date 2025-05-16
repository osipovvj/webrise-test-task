package com.github.osipovvj.webrise_test_task.dto.response;

import com.github.osipovvj.webrise_test_task.entity.Subscription;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "SubscriptionsResponse", description = "Ответ сервера со списком всех сервисов.")
public record SubscriptionsResponse(
        @Schema(description = "Количество сервисов.", example = "20")
        Integer count,

        @Schema(description = "Список сервисов.")
        List<SubscriptionResponse> subscriptions
) {
        public static SubscriptionsResponse toResponse(final List<Subscription> subscriptions) {
                return new SubscriptionsResponse(
                        subscriptions.size(),
                        subscriptions
                                .stream()
                                .map(SubscriptionResponse::toResponse)
                                .toList()
                );
        }
}
