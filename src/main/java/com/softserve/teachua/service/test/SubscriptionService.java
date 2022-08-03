package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.subscription.CreateSubscription;
import com.softserve.teachua.dto.test.user.UserResponse;

import java.util.List;

public interface SubscriptionService {
    void createSubscription(CreateSubscription createSubscription);
    List<UserResponse> getUserResponseByGroupId(Long id);
}
