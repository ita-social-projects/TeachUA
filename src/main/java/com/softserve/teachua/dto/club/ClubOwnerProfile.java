package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClubOwnerProfile implements Convertible {
    private Long id;

    @NotNull
    private User user;
}
