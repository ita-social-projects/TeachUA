package com.softserve.teachua.dto.user_challenge.admin;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserChallengeForAdminGetChallengeDuration {
    Long challengeId;
    Long durationId;
    boolean userExist;
    private LocalDate startDate;
    private LocalDate endDate;
}
