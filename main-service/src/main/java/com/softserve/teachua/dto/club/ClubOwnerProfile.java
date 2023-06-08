package com.softserve.teachua.dto.club;

import com.softserve.commons.util.marker.Convertible;
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
    //todo
    //@NotNull
    //private User user;
}
