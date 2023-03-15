package com.softserve.teachua.dto.user_challenge.profile;

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
public class UserChallengeForProfileDelete implements Convertible {
    @NonNull
    private Long userIdForDelete;

    @NonNull
    private Long challengeIdForDelete;

    @NonNull
    private LocalDate startChallengeDate;

    @NonNull
    private LocalDate endChallengeDate;
}
