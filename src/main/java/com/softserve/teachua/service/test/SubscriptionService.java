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

    /**
     * This method returns true or false depending on whether the user has a subscription to the group
     * which contains a specific test.
     * @param userId - put user id here.
     * @param testId put test id here.
     * @return new {@code boolean}
     * @throws IllegalArgumentException if the parameter is null.
     */
    boolean hasSubscription(Long userId, Long testId);
}
