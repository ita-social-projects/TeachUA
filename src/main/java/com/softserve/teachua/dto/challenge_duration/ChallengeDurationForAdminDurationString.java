package com.softserve.teachua.dto.challenge_duration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChallengeDurationForAdminDurationString {
    private String startDate;

    private String endDate;
}
