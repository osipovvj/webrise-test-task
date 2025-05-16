package com.github.osipovvj.webrise_test_task.service.impl;

import com.github.osipovvj.webrise_test_task.dto.request.ChangeSubscriptionStatusRequest;
import com.github.osipovvj.webrise_test_task.dto.request.UserSubscriptionRequest;
import com.github.osipovvj.webrise_test_task.dto.response.UserSubscriptionResponse;
import com.github.osipovvj.webrise_test_task.dto.response.UserSubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.entity.Subscription;
import com.github.osipovvj.webrise_test_task.entity.User;
import com.github.osipovvj.webrise_test_task.entity.UserSubscription;
import com.github.osipovvj.webrise_test_task.enums.SubscriptionStatus;
import com.github.osipovvj.webrise_test_task.exception.AlreadyExistsException;
import com.github.osipovvj.webrise_test_task.exception.ResourceNotFoundException;
import com.github.osipovvj.webrise_test_task.repository.SubscriptionRepository;
import com.github.osipovvj.webrise_test_task.repository.UserRepository;
import com.github.osipovvj.webrise_test_task.repository.UserSubscriptionRepository;
import com.github.osipovvj.webrise_test_task.service.UserSubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;

    @Override
    public UserSubscriptionResponse addUserSubscription(Long userId, UserSubscriptionRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь с id " + userId + " не найден."));
        Subscription subscription = subscriptionRepository.findById(request.subscriptionId())
                .orElseThrow(() -> new ResourceNotFoundException("Подписка с id " + request.subscriptionId() + " не найдена."));

        if (userSubscriptionRepository.existsByUserIdAndSubscriptionId(userId, subscription.getId())) {
            throw new AlreadyExistsException("Пользователь с id " + userId + " уже подписан на сервис с id " + subscription.getId());
        }

        UserSubscription userSubscription = new UserSubscription();

        userSubscription.setUser(user);
        userSubscription.setSubscription(subscription);
        userSubscription.setPrice(request.price());
        userSubscription.setStatus(SubscriptionStatus.ACTIVE);

        userSubscription = userSubscriptionRepository.save(userSubscription);

        return UserSubscriptionResponse.toResponse(userSubscription);
    }

    @Override
    public UserSubscriptionResponse changeUserSubscriptionStatus(Long userId, Long subscriptionId, ChangeSubscriptionStatusRequest request) {
        UserSubscription userSubscription = userSubscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользовтель с id " + userId + " не подписан на сервис с id " + subscriptionId));

        userSubscription.setStatus(request.subscriptionStatus());

        return UserSubscriptionResponse.toResponse(userSubscriptionRepository.save(userSubscription));
    }

    @Override
    @Transactional(readOnly = true)
    public UserSubscriptionsResponse getUserSubscriptions(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Пользователь с id " + userId + " не найден.");
        }

        return UserSubscriptionsResponse.toResponse(userSubscriptionRepository.findAllByUserId(userId));
    }

    @Override
    public void removeUserSubscription(Long userId, Long subscriptionId) {
        UserSubscription userSubscription = userSubscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользовтель с id " + userId + " не подписан на сервис с id " + subscriptionId));

        userSubscriptionRepository.delete(userSubscription);
    }
}
