package com.softserve.question.controller;

import com.softserve.question.controller.marker.Api;
import com.softserve.question.dto.subscription.CreateSubscription;
import com.softserve.question.dto.subscription.SubscriptionProfile;
import com.softserve.question.service.SubscriptionService;
import static org.springframework.http.HttpStatus.CREATED;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller is for managing subscriptions.
 */
@RestController
@RequestMapping("/api/v1/subscription")
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
    @PostMapping(params = {"testId"})
    public void createSubscriptionByTest(@RequestBody CreateSubscription subscription,
                                         @RequestParam("testId") Long testId) {
        subscriptionService.createSubscriptionByTestId(subscription, testId);
    }

    @ResponseStatus(value = CREATED)
    @PostMapping(params = {"groupId", "userId"})
    public SubscriptionProfile createSubscriptionByGroup(@RequestParam("groupId") Long groupId,
                                                         @RequestParam("userId") Long userId) {
        return subscriptionService.createSubscriptionByUserIdAndGroupId(userId, groupId);
    }

    /**
     * Use this endpoint to delete the subscription.
     *
     * @param groupId - put group id here.
     * @param userId  - put user id here.
     */
    @DeleteMapping(params = {"groupId", "userId"})
    public SubscriptionProfile deleteUserSubscription(@RequestParam("groupId") Long groupId,
                                                      @RequestParam("userId") Long userId) {
        return subscriptionService.deleteSubscriptionByUserIdAndGroupId(userId, groupId);
    }
}
