package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.ResultController;
import com.softserve.teachua.dto.test.subscription.CreateSubscription;
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

import static com.softserve.teachua.utils.test.NullValidator.*;
import static com.softserve.teachua.utils.test.Messages.*;
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
    @Transactional(readOnly = true)
    public List<UserResponse> getUserResponseByGroupId(Long groupId) {
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
            userResponse.add(userResults);
            userResponses.add(userResponse);
        }
        return userResponses;
    }

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
        throw new IllegalArgumentException(
                String.format(INCORRECT_ENROLLMENT_KEY_MESSAGE, enrollmentKey));
    }

    private boolean isActiveSubscription(Subscription subscription) {
        Group group = subscription.getGroup();
        return subscription.getExpirationDate().equals(group.getEndDate());
    }
}
