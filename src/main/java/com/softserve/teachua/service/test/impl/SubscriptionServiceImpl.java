package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.model.test.Subscription;
import com.softserve.teachua.repository.test.SubscriptionRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final GroupService groupService;
    private final UserService userService;

    @Override
    public void createSubscription(String enrollmentKey) {
        Group group = groupService.findByEnrollmentKey(enrollmentKey);
        User user = userService.getCurrentUser();
        Subscription subscription = new Subscription();
        subscription.setGroup(group);
        subscription.setUser(user);
        subscription.setExpirationDate(group.getEndDate());

        subscriptionRepository.save(subscription);
    }
}
