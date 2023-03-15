package com.softserve.teachua.dto.user_challenge.registration;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeForUserCreateWithDate implements Convertible {
    @NonNull
    private Long userId;

    @NonNull
    private Long challengeId;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate endDate;
}
