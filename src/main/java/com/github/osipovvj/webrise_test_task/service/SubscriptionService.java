package com.github.osipovvj.webrise_test_task.service;

import com.github.osipovvj.webrise_test_task.dto.request.SubscriptionRequest;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionResponse;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.dto.response.TopSubscriptionsResponse;

public interface SubscriptionService {
    SubscriptionResponse createSubscription(SubscriptionRequest request);
    SubscriptionResponse updateSubscription(Long id, SubscriptionRequest request);
    SubscriptionsResponse getSubscriptions();
    void deleteSubscription(Long id);
    TopSubscriptionsResponse getTopSubscriptions();
}
