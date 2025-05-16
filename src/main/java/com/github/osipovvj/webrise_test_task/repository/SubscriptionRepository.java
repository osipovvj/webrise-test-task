package com.github.osipovvj.webrise_test_task.repository;

import com.github.osipovvj.webrise_test_task.entity.Subscription;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    boolean existsBySubscriptionName(String name);
    boolean existsBySubscriptionNameAndIdNot(String name, Long id);

    @Query("SELECT s FROM Subscription s LEFT JOIN s.userSubscriptions us GROUP BY s ORDER BY COUNT(us.id) DESC")
    List<Subscription> findTop3ByPopularity(Pageable pageable);
}
