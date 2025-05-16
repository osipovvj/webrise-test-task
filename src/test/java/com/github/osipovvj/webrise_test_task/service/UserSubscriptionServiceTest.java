package com.github.osipovvj.webrise_test_task.service;

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
import com.github.osipovvj.webrise_test_task.service.impl.UserSubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserSubscriptionServiceTest {

    private UserRepository userRepository;
    private SubscriptionRepository subscriptionRepository;
    private UserSubscriptionRepository userSubscriptionRepository;
    private UserSubscriptionServiceImpl service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        subscriptionRepository = mock(SubscriptionRepository.class);
        userSubscriptionRepository = mock(UserSubscriptionRepository.class);
        service = new UserSubscriptionServiceImpl(userRepository, subscriptionRepository, userSubscriptionRepository);
    }

    @Test
    void addUserSubscription_success() {
        Long userId = 1L;
        Long subscriptionId = 2L;
        User user = User.builder().id(userId).username("test").email("t@t.ru").build();
        Subscription subscription = Subscription.builder().id(subscriptionId).subscriptionName("Netflix").build();
        UserSubscriptionRequest request = new UserSubscriptionRequest(subscriptionId, new BigDecimal("9.99"));
        UserSubscription userSubscription = UserSubscription.builder()
                .id(10L)
                .user(user)
                .subscription(subscription)
                .price(new BigDecimal("9.99"))
                .status(SubscriptionStatus.ACTIVE)
                .subscribedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));
        when(userSubscriptionRepository.existsByUserIdAndSubscriptionId(userId, subscriptionId)).thenReturn(false);
        when(userSubscriptionRepository.save(any(UserSubscription.class))).thenReturn(userSubscription);

        UserSubscriptionResponse response = service.addUserSubscription(userId, request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.price()).isEqualTo(new BigDecimal("9.99"));
        assertThat(response.status()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(response.subscription().id()).isEqualTo(subscriptionId);
    }

    @Test
    void addUserSubscription_alreadyExists() {
        Long userId = 1L;
        Long subscriptionId = 2L;
        UserSubscriptionRequest request = new UserSubscriptionRequest(subscriptionId, new BigDecimal("9.99"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(Subscription.builder().id(subscriptionId).build()));
        when(userSubscriptionRepository.existsByUserIdAndSubscriptionId(userId, subscriptionId)).thenReturn(true);

        assertThatThrownBy(() -> service.addUserSubscription(userId, request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Пользователь с id " + userId + " уже подписан на сервис с id " + subscriptionId);
    }

    @Test
    void addUserSubscription_userNotFound() {
        Long userId = 1L;
        Long subscriptionId = 2L;
        UserSubscriptionRequest request = new UserSubscriptionRequest(subscriptionId, new BigDecimal("9.99"));

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addUserSubscription(userId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользователь с id " + userId + " не найден.");
    }

    @Test
    void addUserSubscription_subscriptionNotFound() {
        Long userId = 1L;
        Long subscriptionId = 2L;
        UserSubscriptionRequest request = new UserSubscriptionRequest(subscriptionId, new BigDecimal("9.99"));

        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.addUserSubscription(userId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Подписка с id " + subscriptionId + " не найдена.");
    }

    @Test
    void changeUserSubscriptionStatus_success_middleValue() {
        Long userId = 1L;
        Long subscriptionId = 2L;
        User user = User.builder().id(userId).username("test").email("t@t.ru").build();
        Subscription subscription = Subscription.builder().id(subscriptionId).subscriptionName("Netflix").build();
        UserSubscription userSubscription = UserSubscription.builder()
                .id(10L)
                .user(user)
                .subscription(subscription)
                .status(SubscriptionStatus.ACTIVE)
                .build();
        ChangeSubscriptionStatusRequest request = new ChangeSubscriptionStatusRequest(SubscriptionStatus.INACTIVE);

        when(userSubscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId)).thenReturn(Optional.of(userSubscription));
        when(userSubscriptionRepository.save(userSubscription)).thenReturn(userSubscription);

        UserSubscriptionResponse response = service.changeUserSubscriptionStatus(userId, subscriptionId, request);

        assertThat(response.status()).isEqualTo(SubscriptionStatus.INACTIVE);
    }

    @Test
    void changeUserSubscriptionStatus_notFound() {
        Long userId = 1L;
        Long subscriptionId = 2L;
        ChangeSubscriptionStatusRequest request = new ChangeSubscriptionStatusRequest(SubscriptionStatus.INACTIVE);

        when(userSubscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.changeUserSubscriptionStatus(userId, subscriptionId, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользовтель с id " + userId + " не подписан на сервис с id " + subscriptionId);
    }

    @Test
    void getUserSubscriptions_success_boundary() {
        Long userId = 1L;
        Subscription subscription1 = Subscription.builder().id(101L).subscriptionName("S1").build();
        Subscription subscription2 = Subscription.builder().id(102L).subscriptionName("S2").build();
        User user = User.builder().id(userId).username("test").email("t@t.ru").build();
        UserSubscription us1 = UserSubscription.builder().id(1L).status(SubscriptionStatus.ACTIVE).user(user).subscription(subscription1).build();
        UserSubscription us2 = UserSubscription.builder().id(2L).status(SubscriptionStatus.INACTIVE).user(user).subscription(subscription2).build();

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userSubscriptionRepository.findAllByUserId(userId)).thenReturn(List.of(us1, us2));

        UserSubscriptionsResponse response = service.getUserSubscriptions(userId);

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.subscriptions()).extracting("id").containsExactly(1L, 2L);
    }

    @Test
    void getUserSubscriptions_userNotFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> service.getUserSubscriptions(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользователь с id " + userId + " не найден.");
    }

    @Test
    void removeUserSubscription_success() {
        Long userId = 1L;
        Long subscriptionId = 2L;
        UserSubscription us = UserSubscription.builder().id(10L).build();

        when(userSubscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId)).thenReturn(Optional.of(us));

        service.removeUserSubscription(userId, subscriptionId);

        verify(userSubscriptionRepository).delete(us);
    }

    @Test
    void removeUserSubscription_notFound() {
        Long userId = 1L;
        Long subscriptionId = 2L;

        when(userSubscriptionRepository.findByUserIdAndSubscriptionId(userId, subscriptionId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.removeUserSubscription(userId, subscriptionId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Пользовтель с id " + userId + " не подписан на сервис с id " + subscriptionId);
    }
}
