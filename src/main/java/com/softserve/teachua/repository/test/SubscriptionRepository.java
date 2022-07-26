package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}
