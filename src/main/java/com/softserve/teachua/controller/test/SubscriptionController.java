package com.softserve.teachua.controller.test;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.test.subscription.CreateSubscription;
import com.softserve.teachua.service.test.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.*;
import static org.springframework.http.HttpStatus.*;

/**
 * This controller is for managing the subscriptions.
 * */

@RequiredArgsConstructor
@RestController
public class SubscriptionController implements Api {
    private final SubscriptionService subscriptionService;

    /**
     * Use this endpoint to create new subscription.
     *
     * @param subscription - post subscription details here.
     */
    @ResponseStatus(value = CREATED)
    @PostMapping(value = "/subscriptions", consumes = APPLICATION_JSON_VALUE)
    public void createSubscription(@RequestBody CreateSubscription subscription) {
        subscriptionService.createSubscription(subscription);
    }
}
