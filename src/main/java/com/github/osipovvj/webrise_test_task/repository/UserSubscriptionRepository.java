package com.github.osipovvj.webrise_test_task.repository;

import com.github.osipovvj.webrise_test_task.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    boolean existsByUserIdAndSubscriptionId(Long userId, Long subscriptionId);
    List<UserSubscription> findAllByUserId(Long userId);
    Optional<UserSubscription> findByUserIdAndSubscriptionId(Long userId, Long subscriptionId);
}
