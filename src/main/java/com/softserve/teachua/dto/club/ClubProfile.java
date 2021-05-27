package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
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

    private String contacts;
  
    private Boolean isApproved;

    private Long userId;

    private Long centerId;

    private Long clubExternalId;

    private Long centerExternalId;
}
