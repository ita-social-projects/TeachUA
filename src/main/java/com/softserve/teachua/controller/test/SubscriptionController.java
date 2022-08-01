package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/subscriptions")
@RestController
public class SubscriptionController implements Api {
    private final SubscriptionService subscriptionService;

    @PostMapping
    public void createSubscription(@RequestParam String enrollmentKey) {
        subscriptionService.createSubscription(enrollmentKey);
    }
}
