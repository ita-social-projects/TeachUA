package com.softserve.teachua.dto.challenge_registration;

import com.softserve.teachua.utils.validations.CheckRussian;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeRegistrationRequest {
    @Min(0)
    @NotNull
    private Long userId;
    @Min(0)
    @NotNull
    private Long challengeId;
    @CheckRussian
    private String comment;
}
