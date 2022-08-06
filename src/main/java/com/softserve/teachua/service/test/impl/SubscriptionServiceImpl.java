package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.controller.test.TestController;
import com.softserve.teachua.converter.DtoConverter;
import com.softserve.teachua.dto.test.result.UserResult;
import com.softserve.teachua.dto.test.subscription.CreateSubscription;
import com.softserve.teachua.dto.test.user.UserResponse;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Group;
import com.softserve.teachua.model.test.Result;
import com.softserve.teachua.model.test.Subscription;
import com.softserve.teachua.repository.test.SubscriptionRepository;
import com.softserve.teachua.service.UserService;
import com.softserve.teachua.service.test.GroupService;
import com.softserve.teachua.service.test.ResultService;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final DtoConverter dtoConverter;
    private final ResultService resultService;

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

    @Override
    public List<UserResponse> getUserResponseByGroupId(Long id) {
        List<Subscription> subscriptions = subscriptionRepository.findSubscriptionsByGroupId(id);
        List<UserResponse> userResponses = new ArrayList<>();
        for (Subscription s: subscriptions) {
            User user = s.getUser();
            UserResponse userResponse = dtoConverter.convertToDto(user, UserResponse.class);

            userResponse.setUserResults(new ArrayList<>());
            List<Result> results = resultService.findByUser(user);
            for(Result r: results){
                UserResult userResult = dtoConverter.convertToDto(r, UserResult.class);
                Link resultReview = linkTo(methodOn(TestController.class).getTestResult(r.getTest().getId(), r.getId()))
                        .withSelfRel();
                userResult.add(resultReview);
                userResponse.getUserResults().add(userResult);
            }
            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @Override
    public boolean hasSubscription(Long userId, Long testId) {
        List<Long> userGroupsIds = subscriptionRepository.findAllByUserId(userId).stream()
                .map(Subscription::getGroup)
                .map(Group::getId)
                .collect(Collectors.toList());
        List<Group> testGroups = groupService.findAllByTestId(testId);

        if (Objects.isNull(userId) || Objects.isNull(testId))
            throw new IllegalArgumentException("User id or test id can't be null");

        return testGroups.stream()
                .anyMatch(group -> userGroupsIds.contains(group.getId()));
    }
}
