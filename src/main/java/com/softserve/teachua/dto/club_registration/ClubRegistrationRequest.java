package com.softserve.teachua.dto.club_registration;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubRegistrationRequest {
    @NotNull
    private List<Long> childIds;
    @NotNull
    @Min(0)
    private Long clubId;

}
