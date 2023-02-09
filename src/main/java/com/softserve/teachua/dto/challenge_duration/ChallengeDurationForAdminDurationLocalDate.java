package com.softserve.teachua.dto.challenge_duration;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChallengeDurationForAdminDurationLocalDate {
    private LocalDate startDate;

    private LocalDate endDate;
}
