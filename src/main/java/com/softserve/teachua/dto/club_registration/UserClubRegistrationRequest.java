package com.softserve.teachua.dto.club_registration;

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
public class UserClubRegistrationRequest {
    @Min(0)
    @NotNull
    private Long userId;
    @Min(0)
    @NotNull
    private Long clubId;
}
