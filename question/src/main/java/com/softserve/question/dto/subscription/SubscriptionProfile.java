package com.softserve.question.dto.subscription;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@Setter
public class SubscriptionProfile {
    private String groupTitle;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate expirationDate;
}
