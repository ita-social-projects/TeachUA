package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.subscription.CreateSubscription;
import com.softserve.teachua.dto.test.subscription.SubscriptionProfile;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This controller is for managing subscriptions.
 * */

@RequiredArgsConstructor
@RestController
public class SubscriptionController implements Api {
    private final SubscriptionService subscriptionService;

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
