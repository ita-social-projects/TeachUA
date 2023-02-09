package com.softserve.teachua.dto.user_challenge.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeForUserCreate {
    @NonNull
    private Long userId;

    @NonNull
    private Long challengeId;

    @NonNull
    private Long durationId;
}
