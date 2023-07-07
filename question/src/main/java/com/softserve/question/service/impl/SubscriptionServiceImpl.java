package com.softserve.question.service.impl;

import com.softserve.question.dto.subscription.CreateSubscription;
import com.softserve.question.dto.subscription.SubscriptionProfile;
import com.softserve.question.model.Group;
import com.softserve.question.model.Subscription;
import com.softserve.question.repository.SubscriptionRepository;
import com.softserve.commons.security.UserPrincipal;
import com.softserve.question.service.GroupService;
import com.softserve.question.service.SubscriptionService;
import static com.softserve.question.util.Messages.INCORRECT_ENROLLMENT_KEY_MESSAGE;
import static com.softserve.question.util.Messages.NO_SUBSCRIPTION_MESSAGE;
import static com.softserve.question.util.Messages.SUBSCRIPTION_EXISTS_MESSAGE;
import static com.softserve.question.util.Messages.TEST_WITHOUT_GROUP_MESSAGE;
import static com.softserve.question.util.validation.NullValidator.checkNullIds;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final GroupService groupService;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, GroupService groupService) {
        this.subscriptionRepository = subscriptionRepository;
        this.groupService = groupService;
    }

    @Override
    public Subscription findByUserIdAndGroupId(Long userId, Long groupId) {
        checkNullIds(userId, groupId);
        return subscriptionRepository.findByUserIdAndGroupId(userId, groupId);
    }

    @Override
    public SubscriptionProfile createSubscriptionByTestId(CreateSubscription createSubscription, Long testId) {
        checkNullIds(createSubscription, testId);
        List<Group> groups = groupService.findAllByTestId(testId);
        String enrollmentKey = createSubscription.getEnrollmentKey();

        if (groups.isEmpty()) {
            throw new NoSuchElementException(String.format(TEST_WITHOUT_GROUP_MESSAGE, testId));
        }

        for (Group group : groups) {
            if (group.getEnrollmentKey().equals(enrollmentKey)) {
                Long userId =
                        ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
                Subscription subscription = generateSubscription(group, userId);
                subscriptionRepository.save(subscription);
                log.info("**/Subscription has been created. {}", subscription);
                return generateSubscriptionProfile(subscription);
            }
        }
        throw new IllegalArgumentException(
                String.format(INCORRECT_ENROLLMENT_KEY_MESSAGE, enrollmentKey));
    }

    @Override
    public SubscriptionProfile createSubscriptionByUserIdAndGroupId(Long userId, Long groupId) {
        checkNullIds(userId, groupId);
        Group group = groupService.findGroupById(groupId);
        Subscription subscription = generateSubscription(group, userId);
        subscriptionRepository.save(subscription);
        log.info("**/Subscription has been created. {}", subscription);
        return generateSubscriptionProfile(subscription);
    }

    @Override
    public SubscriptionProfile deleteSubscriptionByUserIdAndGroupId(Long userId, Long groupId) {
        checkNullIds(userId, groupId);
        List<Subscription> subscriptions = subscriptionRepository.findAllByUserIdAndGroupId(userId, groupId);
        Subscription subscription = subscriptions.stream()
                .filter(this::isActiveSubscription)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException(
                        String.format(NO_SUBSCRIPTION_MESSAGE, userId, groupId)));
        subscriptionRepository.delete(subscription);
        return generateSubscriptionProfile(subscription);
    }

    private boolean isActiveSubscription(Subscription subscription) {
        Group group = subscription.getGroup();
        return subscription.getExpirationDate().equals(group.getEndDate());
    }

    private boolean hasActiveSubscription(Long userId, Group group) {
        Subscription subscription = findByUserIdAndGroupId(userId, group.getId());
        return !Objects.isNull(subscription) && isActiveSubscription(subscription);
    }

    private void checkSubscription(Long userId, Group group) {
        if (hasActiveSubscription(userId, group)) {
            throw new IllegalStateException(
                    String.format(SUBSCRIPTION_EXISTS_MESSAGE, userId));
        }
    }

    private Subscription generateSubscription(Group group, Long userId) {
        checkSubscription(userId, group);
        Subscription subscription = new Subscription();
        subscription.setGroup(group);
        subscription.setUserId(userId);
        subscription.setExpirationDate(group.getEndDate());
        return subscription;
    }

    private SubscriptionProfile generateSubscriptionProfile(Subscription subscription) {
        SubscriptionProfile subscriptionProfile = new SubscriptionProfile();
        subscriptionProfile.setExpirationDate(subscription.getExpirationDate());
        subscriptionProfile.setGroupTitle(subscription.getGroup().getTitle());
        return subscriptionProfile;
    }
}
