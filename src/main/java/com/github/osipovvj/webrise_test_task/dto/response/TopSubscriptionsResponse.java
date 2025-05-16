package com.github.osipovvj.webrise_test_task.dto.response;

import com.github.osipovvj.webrise_test_task.entity.Subscription;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "TopThreeSubscriptionsResponse", description = "Ответ сервера со списком топ 3 самых популярных сервиса.")
public record TopSubscriptionsResponse(
        @Schema(description = "Список самых популярных сервисов.")
        List<SubscriptionResponse> subscriptions
) {
        public static TopSubscriptionsResponse toResponse(final List<Subscription> subscriptions) {
                return new TopSubscriptionsResponse(
                        subscriptions
                                .stream()
                                .map(SubscriptionResponse::toResponse)
                                .toList()
                );
        }
}
