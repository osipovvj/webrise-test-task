package com.github.osipovvj.webrise_test_task.service;

import com.github.osipovvj.webrise_test_task.dto.request.SubscriptionRequest;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionResponse;
import com.github.osipovvj.webrise_test_task.dto.response.SubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.dto.response.TopSubscriptionsResponse;
import com.github.osipovvj.webrise_test_task.entity.Subscription;
import com.github.osipovvj.webrise_test_task.exception.AlreadyExistsException;
import com.github.osipovvj.webrise_test_task.exception.ResourceNotFoundException;
import com.github.osipovvj.webrise_test_task.repository.SubscriptionRepository;
import com.github.osipovvj.webrise_test_task.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    private SubscriptionRepository subscriptionRepository;
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void setUp() {
        subscriptionRepository = mock(SubscriptionRepository.class);
        subscriptionService = new SubscriptionServiceImpl(subscriptionRepository);
    }

    @Test
    void createSubscription_success() {
        SubscriptionRequest request = new SubscriptionRequest("Netflix", "Netflix Premium", "https://netflix.com/premium");
        Subscription subscription = Subscription.builder()
                .id(1L)
                .subscriptionName("Netflix")
                .serviceName("Netflix Premium")
                .serviceUrl("https://netflix.com/premium")
                .createdAt(LocalDateTime.now())
                .build();

        when(subscriptionRepository.existsBySubscriptionName("Netflix")).thenReturn(false);
        when(subscriptionRepository.save(ArgumentMatchers.any(Subscription.class))).thenReturn(subscription);

        SubscriptionResponse response = subscriptionService.createSubscription(request);

        assertThat(response.subscriptionName()).isEqualTo("Netflix");
        assertThat(response.serviceName()).isEqualTo("Netflix Premium");
        assertThat(response.serviceUrl()).isEqualTo("https://netflix.com/premium");
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void createSubscription_alreadyExists() {
        SubscriptionRequest request = new SubscriptionRequest("Netflix", "Netflix Premium", "https://netflix.com/premium");
        when(subscriptionRepository.existsBySubscriptionName("Netflix")).thenReturn(true);

        assertThatThrownBy(() -> subscriptionService.createSubscription(request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Сервис с именем Netflix уже существует.");
    }

    @Test
    void updateSubscription_success_middleValue() {
        Long id = 10L;
        Subscription existing = Subscription.builder()
                .id(id)
                .subscriptionName("OldName")
                .serviceName("Old Service")
                .serviceUrl("https://old.com")
                .createdAt(LocalDateTime.now())
                .build();

        SubscriptionRequest request = new SubscriptionRequest("NewName", "New Service", "https://new.com");
        Subscription updated = Subscription.builder()
                .id(id)
                .subscriptionName("NewName")
                .serviceName("New Service")
                .serviceUrl("https://new.com")
                .createdAt(existing.getCreatedAt())
                .build();

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(subscriptionRepository.existsBySubscriptionNameAndIdNot("NewName", id)).thenReturn(false);
        when(subscriptionRepository.save(existing)).thenReturn(updated);

        SubscriptionResponse response = subscriptionService.updateSubscription(id, request);

        assertThat(response.subscriptionName()).isEqualTo("NewName");
        assertThat(response.serviceName()).isEqualTo("New Service");
        assertThat(response.serviceUrl()).isEqualTo("https://new.com");
    }

    @Test
    void updateSubscription_nameAlreadyExists() {
        Long id = 1L;
        Subscription existing = Subscription.builder()
                .id(id)
                .subscriptionName("OldName")
                .serviceName("Old Service")
                .serviceUrl("https://old.com")
                .build();
        SubscriptionRequest request = new SubscriptionRequest("OtherName", "Old Service", "https://old.com");

        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(subscriptionRepository.existsBySubscriptionNameAndIdNot("OtherName", id)).thenReturn(true);

        assertThatThrownBy(() -> subscriptionService.updateSubscription(id, request))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("Сервис с названием OtherName уже существует.");
    }

    @Test
    void updateSubscription_notFound() {
        when(subscriptionRepository.findById(100L)).thenReturn(Optional.empty());
        SubscriptionRequest request = new SubscriptionRequest("Any", "Any", "https://any.com");

        assertThatThrownBy(() -> subscriptionService.updateSubscription(100L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Сервис с id 100 не найден.");
    }

    @Test
    void deleteSubscription_success() {
        Long id = 5L;
        when(subscriptionRepository.existsById(id)).thenReturn(true);

        subscriptionService.deleteSubscription(id);

        verify(subscriptionRepository).deleteById(id);
    }

    @Test
    void deleteSubscription_notFound() {
        when(subscriptionRepository.existsById(123L)).thenReturn(false);

        assertThatThrownBy(() -> subscriptionService.deleteSubscription(123L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Сервис с id 123 не найден.");
    }

    @Test
    void getSubscriptions_success() {
        Subscription s1 = Subscription.builder().id(1L).subscriptionName("A").serviceName("A1").serviceUrl("url1").createdAt(LocalDateTime.now()).build();
        Subscription s2 = Subscription.builder().id(2L).subscriptionName("B").serviceName("B1").serviceUrl("url2").createdAt(LocalDateTime.now()).build();

        when(subscriptionRepository.findAll()).thenReturn(List.of(s1, s2));

        SubscriptionsResponse response = subscriptionService.getSubscriptions();

        assertThat(response.count()).isEqualTo(2);
        assertThat(response.subscriptions()).extracting("subscriptionName").containsExactly("A", "B");
    }

    @Test
    void getTopSubscriptions_success_boundary() {
        Subscription s1 = Subscription.builder().id(1L).subscriptionName("A").serviceName("A1").serviceUrl("url1").createdAt(LocalDateTime.now()).build();
        Subscription s2 = Subscription.builder().id(2L).subscriptionName("B").serviceName("B1").serviceUrl("url2").createdAt(LocalDateTime.now()).build();
        Subscription s3 = Subscription.builder().id(3L).subscriptionName("C").serviceName("C1").serviceUrl("url3").createdAt(LocalDateTime.now()).build();

        when(subscriptionRepository.findTop3ByPopularity(PageRequest.of(0, 3))).thenReturn(List.of(s1, s2, s3));

        TopSubscriptionsResponse response = subscriptionService.getTopSubscriptions();

        assertThat(response.subscriptions()).hasSize(3);
        assertThat(response.subscriptions()).extracting("subscriptionName").containsExactly("A", "B", "C");
    }

    @Test
    void getTopSubscriptions_lessThanThree() {
        Subscription s1 = Subscription.builder().id(1L).subscriptionName("A").serviceName("A1").serviceUrl("url1").createdAt(LocalDateTime.now()).build();

        when(subscriptionRepository.findTop3ByPopularity(PageRequest.of(0, 3))).thenReturn(List.of(s1));

        TopSubscriptionsResponse response = subscriptionService.getTopSubscriptions();

        assertThat(response.subscriptions()).hasSize(1);
        assertThat(response.subscriptions().get(0).subscriptionName()).isEqualTo("A");
    }
}
