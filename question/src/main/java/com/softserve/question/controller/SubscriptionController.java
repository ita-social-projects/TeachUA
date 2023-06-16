package com.softserve.question.controller;

import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.subscription.CreateSubscription;
import com.softserve.question.dto.subscription.SubscriptionProfile;
import com.softserve.question.service.SubscriptionService;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing subscriptions.
 */
@RestController
public class SubscriptionController implements Api {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Use this endpoint to create new subscription.
     *
     * @param subscription - put subscription details here.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/subscriptions/tests/{testId}", consumes = APPLICATION_JSON_VALUE)
    public void createSubscriptionByTest(@RequestBody CreateSubscription subscription,
                                         @PathVariable Long testId) {
        subscriptionService.createSubscriptionByTestId(subscription, testId);
    }

    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/subscriptions/groups/{groupId}/users/{userId}")
    public SubscriptionProfile createSubscriptionByGroup(@PathVariable Long groupId,
                                                         @PathVariable Long userId) {
        return subscriptionService.createSubscriptionByUserIdAndGroupId(userId, groupId);
    }

    /**
     * Use this endpoint to delete the subscription.
     *
     * @param groupId - put group id here.
     * @param userId  - put user id here.
     */
    @DeleteMapping(value = "/subscriptions/groups/{groupId}/users/{userId}")
    public SubscriptionProfile deleteUserSubscription(@PathVariable Long groupId,
                                                      @PathVariable Long userId) {
        return subscriptionService.deleteSubscriptionByUserIdAndGroupId(userId, groupId);
    }
}
