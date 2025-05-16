package com.github.osipovvj.webrise_test_task.dto.response;

import com.github.osipovvj.webrise_test_task.entity.UserSubscription;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "UserSubscriptionsResponse", description = "Ответ сервера со списком пользовательских подписок.")
public record UserSubscriptionsResponse(
        @Schema(description = "Количество подписок пользователя.", example = "10")
        Integer count,

        @Schema(description = "Список пользовательских подписок.")
        List<UserSubscriptionResponse> subscriptions
) {
        public static UserSubscriptionsResponse toResponse(final List<UserSubscription> subscriptions) {
                return new UserSubscriptionsResponse(
                        subscriptions.size(),
                        subscriptions
                                .stream()
                                .map(UserSubscriptionResponse::toResponse)
                                .toList()
                );
        }
}
