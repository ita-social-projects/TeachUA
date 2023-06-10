package com.softserve.question.repository;

import com.softserve.question.model.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Provides an interface for managing {@link Subscription} model.
 */

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByGroupId(Long groupId);

    List<Subscription> findAllByUserId(Long userId);

    List<Subscription> findAllByUserIdAndGroupId(Long userId, Long groupId);

    Subscription findByUserIdAndGroupId(Long userId, Long groupId);
}
