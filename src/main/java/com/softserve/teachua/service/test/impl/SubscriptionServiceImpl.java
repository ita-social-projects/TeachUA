package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.ResultController;
import com.softserve.teachua.controller.test.SubscriptionController;
import com.softserve.teachua.dto.test.subscription.CreateSubscription;
import com.softserve.teachua.dto.test.subscription.SubscriptionProfile;
import com.softserve.teachua.dto.test.user.UserResponse;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.model.test.Subscription;
import com.softserve.teachua.repository.test.SubscriptionRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import static com.softserve.teachua.utils.test.Messages.*;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNull;
import static com.softserve.teachua.utils.test.validation.NullValidator.checkNullIds;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public Subscription findByUserIdAndGroupId(Long userId, Long groupId) {
        return subscriptionRepository.findByUserIdAndGroupId(userId, groupId);
    }

    @Override
    public SubscriptionProfile createSubscriptionByTestId(CreateSubscription createSubscription, Long testId) {
        checkNull(createSubscription, "Create subscription dto");
        List<Group> groups = groupService.findAllByTestId(testId);
        String enrollmentKey = createSubscription.getEnrollmentKey();

        for (Group group : groups) {
            if (group.getEnrollmentKey().equals(enrollmentKey)) {
                User user = userService.getCurrentUser();
                checkSubscription(user, group);
                Subscription subscription = new Subscription();
                subscription.setGroup(group);
                subscription.setUser(user);
                subscription.setExpirationDate(group.getEndDate());
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
        User user = userService.getUserById(userId);
        Group group = groupService.findGroupById(groupId);
        checkSubscription(user, group);
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setGroup(group);
        subscription.setExpirationDate(group.getEndDate());
        subscriptionRepository.save(subscription);
        log.info("**/Subscription has been created. {}", subscription.toString());
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
                        String.format(NO_SUBSCRIPTION_MESSAGE, groupId, userId)));
        subscriptionRepository.delete(subscription);
        return generateSubscriptionProfile(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUserResponsesByGroupId(Long groupId) {
        checkNull(groupId, "Group id");
        List<Subscription> subscriptions = subscriptionRepository.findAllByGroupId(groupId);
        List<UserResponse> userResponses = new ArrayList<>();

        for (Subscription subscription : subscriptions) {
            if (!isActiveSubscription(subscription)) continue;
            User user = subscription.getUser();
            UserResponse userResponse = modelMapper.map(user, UserResponse.class);
            Link userResults = linkTo(methodOn(ResultController.class)
                    .getUserResults(groupId, user.getId()))
                    .withRel("results");
            Link dropUser = linkTo(methodOn(SubscriptionController.class)
                    .deleteUserSubscription(groupId, user.getId()))
                    .withRel("drop");
            userResponse.add(userResults, dropUser);
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    private boolean isActiveSubscription(Subscription subscription) {
        Group group = subscription.getGroup();
        return subscription.getExpirationDate().equals(group.getEndDate());
    }

    private boolean hasActiveSubscription(User user, Group group) {
        Subscription subscription = findByUserIdAndGroupId(user.getId(), group.getId());
        return !Objects.isNull(subscription) && isActiveSubscription(subscription);
    }

    private void checkSubscription(User user, Group group) {
        if (hasActiveSubscription(user, group)) {
            throw new IllegalStateException(
                    String.format(SUBSCRIPTION_EXISTS_MESSAGE, user.getFirstName(), user.getLastName()));
        }
    }

    private SubscriptionProfile generateSubscriptionProfile(Subscription subscription) {
        SubscriptionProfile subscriptionProfile = new SubscriptionProfile();
        subscriptionProfile.setExpirationDate(subscription.getExpirationDate());
        subscriptionProfile.setUsername(subscription.getUser().getFirstName());
        subscriptionProfile.setGroupTitle(subscription.getGroup().getTitle());
        return subscriptionProfile;
    }
}
