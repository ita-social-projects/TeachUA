package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
