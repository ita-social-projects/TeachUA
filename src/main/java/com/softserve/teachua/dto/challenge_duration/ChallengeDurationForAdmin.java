package com.softserve.teachua.dto.challenge_duration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChallengeDurationForAdmin {
    private Long challengeId;

    private String challengeName;

    private Boolean isActive;
}
