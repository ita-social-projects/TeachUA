package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClubProfile implements Convertible {
    private Long id;

    private List<String> categoriesName;

    private List<LocationProfile> locations;

    private String description;

    private String name;

    private Integer ageFrom;

    private Integer ageTo;

    private String urlBackground;

    private String urlLogo;

    private Boolean isOnline;

    // private Center center;
}
