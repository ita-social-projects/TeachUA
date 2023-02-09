package com.softserve.teachua.dto.user_challenge_status;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeStatusDelete {
    @NotBlank
    @Size(min = 2, max = 100)
    private String statusName;
}
