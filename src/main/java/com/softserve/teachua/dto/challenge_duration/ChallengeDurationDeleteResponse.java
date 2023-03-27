package com.softserve.teachua.dto.challenge_duration;

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
public class ChallengeDurationDeleteResponse implements Convertible {
    private LocalDate startDate;

    private LocalDate endDate;
}
