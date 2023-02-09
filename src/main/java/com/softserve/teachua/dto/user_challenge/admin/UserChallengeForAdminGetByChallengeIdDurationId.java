package com.softserve.teachua.dto.user_challenge.admin;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeForAdminGetByChallengeIdDurationId implements Convertible {
    private Long challengeId;

    private Long durationId;
}
