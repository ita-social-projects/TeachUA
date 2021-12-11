package com.softserve.teachua.tools.transfer_model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@With
public class ChallengeTransfer {
    private String name;
    private String title;
    private String description;
    private String picture;
    private Long sortNumber;
    private String registrationLink;
    private Boolean isActive;
}
