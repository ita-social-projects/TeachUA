package com.softserve.teachua.dto.challenge_registration;

import com.softserve.teachua.utils.validations.CheckRussian;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildrenChallengeRegistrationRequest {
    @NotNull
    private List<Long> childIds;
    @NotNull
    @Min(0)
    private Long challengeId;
    @CheckRussian
    private String comment;
}
