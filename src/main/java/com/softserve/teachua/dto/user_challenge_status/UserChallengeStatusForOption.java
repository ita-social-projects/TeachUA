package com.softserve.teachua.dto.user_challenge_status;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeStatusForOption implements Convertible {
    private String label;

    private String value;
}
