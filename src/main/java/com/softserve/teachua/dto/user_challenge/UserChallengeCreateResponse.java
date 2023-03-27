package com.softserve.teachua.dto.user_challenge;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeCreateResponse implements Convertible {
    private String challengeName;

    private LocalDate startDate;

    private LocalDate endDate;
}
