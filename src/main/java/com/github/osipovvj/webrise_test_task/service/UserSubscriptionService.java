package com.github.osipovvj.webrise_test_task.service;

import com.github.osipovvj.webrise_test_task.dto.request.ChangeSubscriptionStatusRequest;
import com.github.osipovvj.webrise_test_task.dto.request.UserSubscriptionRequest;
import com.github.osipovvj.webrise_test_task.dto.response.UserSubscriptionResponse;
import com.github.osipovvj.webrise_test_task.dto.response.UserSubscriptionsResponse;

public interface UserSubscriptionService {
    UserSubscriptionResponse addUserSubscription(Long userId, UserSubscriptionRequest request);
    UserSubscriptionResponse changeUserSubscriptionStatus(Long userId, Long subscriptionId, ChangeSubscriptionStatusRequest request);
    UserSubscriptionsResponse getUserSubscriptions(Long userId);
    void removeUserSubscription(Long userId, Long subscriptionId);
}
