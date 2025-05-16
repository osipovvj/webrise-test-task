package com.github.osipovvj.webrise_test_task.service.impl;

import com.github.osipovvj.webrise_test_task.dto.request.SubscriptionRequest;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionResponse;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.dto.response.TopSubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.entity.Subscription;
import com.github.osipovvj.webrise_test_task.exception.AlreadyExistsException;
import com.github.osipovvj.webrise_test_task.exception.ResourceNotFoundException;
import com.github.osipovvj.webrise_test_task.repository.SubscriptionRepository;
import com.github.osipovvj.webrise_test_task.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        if (subscriptionRepository.existsBySubscriptionName(request.subscriptionName())) {
            throw new AlreadyExistsException("Сервис с именем " + request.subscriptionName() + " уже существует.");
        }

        Subscription subscription = Subscription.builder()
                .subscriptionName(request.subscriptionName())
                .serviceName(request.serviceName())
                .serviceUrl(request.serviceUrl())
                .build();

        return SubscriptionResponse.toResponse(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionResponse updateSubscription(Long id, SubscriptionRequest request) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Сервис с id " + id + " не найден."));

        if (!subscription.getSubscriptionName().equals(request.subscriptionName())) {
            if (subscriptionRepository.existsBySubscriptionNameAndIdNot(request.subscriptionName(), id)) {
                throw new AlreadyExistsException("Сервис с названием " + request.subscriptionName() + " уже существует.");
            }
        }

        subscription.setSubscriptionName(request.subscriptionName());
        subscription.setServiceName(request.serviceName());
        subscription.setServiceUrl(request.serviceUrl());

        return SubscriptionResponse.toResponse(subscriptionRepository.save(subscription));
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionsResponse getSubscriptions() {
        return SubscriptionsResponse.toResponse(subscriptionRepository.findAll());
    }

    @Override
    public void deleteSubscription(Long id) {
        if (!subscriptionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Сервис с id " + id + " не найден.");
        }

        subscriptionRepository.deleteById(id);
    }

    @Override
    public TopSubscriptionsResponse getTopSubscriptions() {
        return TopSubscriptionsResponse.toResponse(subscriptionRepository.findTop3ByPopularity(PageRequest.of(0, 3)));
    }
}
