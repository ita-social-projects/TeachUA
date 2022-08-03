package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.dto.test.subscription.CreateSubscription;
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

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final GroupService groupService;
    private final UserService userService;

    @Override
    public void createSubscription(CreateSubscription createSubscription) {
        List<Group> groups = groupService.findAllByTestId(createSubscription.getTestId());
        String enrollmentKey = createSubscription.getEnrollmentKey();

        for (Group group : groups) {
            if (group.getEnrollmentKey().equals(enrollmentKey)) {
                User user = userService.getCurrentUser();
                Subscription subscription = new Subscription();
                subscription.setGroup(group);
                subscription.setUser(user);
                subscription.setExpirationDate(group.getEndDate());
                subscriptionRepository.save(subscription);
                return;
            }
        }
        throw new IllegalArgumentException("Your key is incorrect");
    }
}
