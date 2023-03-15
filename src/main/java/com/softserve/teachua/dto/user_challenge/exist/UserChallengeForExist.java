package com.softserve.teachua.dto.user_challenge.exist;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeForExist implements Convertible {
    private Long userId;

    private Long challengeDurationId;
}
