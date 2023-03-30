package com.softserve.teachua.dto.user_challenge_status;

import com.softserve.teachua.dto.marker.Convertible;
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
public class UserChallengeStatusAdd implements Convertible {
    @NotBlank
    @Size(min = 2, max = 100)
    private String statusName;

    @NotBlank
    @Size(min = 2, max = 100)
    private String statusTitle;
}
