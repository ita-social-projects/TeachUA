package com.softserve.question.service;

import com.softserve.question.dto.subscription.CreateSubscription;
import com.softserve.question.dto.subscription.SubscriptionProfile;
import com.softserve.question.dto.user.UserResponse;
import com.softserve.question.model.Subscription;
import java.util.List;

/**
 * This interface contains all methods needed to manage subscriptions.
 */
public interface SubscriptionService {
    /**
     * Find Subscription entity by user id and group id.
     *
     * @param userId user id
     * @param groupId group id
     * @return founded Subscription entity
     */
    Subscription findByUserIdAndGroupId(Long userId, Long groupId);

    /**
     * This method adds user subscription on a group if enrollment key was correct.
     *
     * @param createSubscription - put body of dto {@code CreateSubscription}.
     * @param testId             - put test id here.
     * @throws IllegalArgumentException if enrollment key is incorrect.
     */
    SubscriptionProfile createSubscriptionByTestId(CreateSubscription createSubscription, Long testId);

    /**
     * This method adds user subscription on a group if enrollment key was correct.
     *
     * @param userId  - put user id.
     * @param groupId - put group id.
     * @return new {@code SubscriptionProfile}
     * @throws IllegalArgumentException if enrollment key is incorrect.
     */
    SubscriptionProfile createSubscriptionByUserIdAndGroupId(Long userId, Long groupId);

    /**
     * Get a list of dto {@code List<UserResponse>} of all users of a group.
     *
     * @param id group id
     * @return a list of dto
     */
    List<UserResponse> getUserResponsesByGroupId(Long id);

    /**
     * This method deletes subscription by user id and group id.
     *
     * @param userId  - put user id.
     * @param groupId - put group id.
     */
    SubscriptionProfile deleteSubscriptionByUserIdAndGroupId(Long userId, Long groupId);
}
