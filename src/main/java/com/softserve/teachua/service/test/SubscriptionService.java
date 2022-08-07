package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.subscription.CreateSubscription;
import com.softserve.teachua.dto.test.user.UserResponse;

import java.util.List;

/**
 * This interface contains all methods needed to manage subscriptions.
 */
public interface SubscriptionService {
    /**
     * This method adds user subscription on a group if enrollment key was correct.
     * @param createSubscription - put body of dto {@code CreateSubscription}.
     * @throws IllegalArgumentException if enrollment key is incorrect.
     */
    void createSubscription(CreateSubscription createSubscription);

    /**
     * This method returns list of dto {@code List<UserResponse>} of all users of a group.
     * @param id - put group id.
     * @return new {@code List<UserResponse>}.
     */
    List<UserResponse> getUserResponseByGroupId(Long id);
}
