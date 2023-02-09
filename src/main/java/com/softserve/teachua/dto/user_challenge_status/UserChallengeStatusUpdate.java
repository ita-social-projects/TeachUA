package com.softserve.teachua.dto.user_challenge_status;

import com.softserve.teachua.dto.marker.Convertible;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeStatusUpdate implements Convertible {
    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String statusName;
}
