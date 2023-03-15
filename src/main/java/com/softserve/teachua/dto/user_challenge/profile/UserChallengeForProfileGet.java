package com.softserve.teachua.dto.user_challenge.profile;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserChallengeForProfileGet implements Convertible {
    private Long id;

    private Long challengeId;

    private String challengeName;

    private LocalDate registrationChallengeDate;

    private LocalDate startChallengeDate;

    private LocalDate endChallengeDate;

    private String userChallengeStatus;
}



