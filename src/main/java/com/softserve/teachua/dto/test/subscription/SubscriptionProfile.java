package com.softserve.teachua.dto.test.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SubscriptionProfile {
    private String username;
    private String groupTitle;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;
}
