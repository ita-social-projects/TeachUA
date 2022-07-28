package com.softserve.teachua.repository.test;

import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.model.test.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findSubscriptionsByGroup(Group group);
}
