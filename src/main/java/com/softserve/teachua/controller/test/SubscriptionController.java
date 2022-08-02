package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SubscriptionController implements Api {
    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions")
    public void createSubscription(@RequestParam String enrollmentKey) {
        subscriptionService.createSubscription(enrollmentKey);
    }
}
