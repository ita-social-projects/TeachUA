package com.softserve.teachua.dto.test.subscription;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionProfile {
    private String username;
    private String groupTitle;
    private LocalDate expirationDate;
}
