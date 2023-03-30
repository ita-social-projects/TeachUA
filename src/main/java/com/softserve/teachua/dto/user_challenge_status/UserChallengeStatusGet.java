package com.softserve.teachua.dto.user_challenge_status;

import com.softserve.teachua.dto.marker.Convertible;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeStatusGet implements Convertible {
    @NotNull
    private Long id;

    @NotBlank
    private String statusName;
    @NotBlank
    private String statusTitle;
}
