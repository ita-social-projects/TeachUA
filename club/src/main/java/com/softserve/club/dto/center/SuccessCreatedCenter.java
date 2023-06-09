package com.softserve.club.dto.center;

import com.softserve.club.dto.location.LocationProfile;
import com.softserve.commons.util.marker.Convertible;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedCenter implements Convertible {
    private Long id;

    private Long userId;

    private String name;

    private String email;

    private String phones;

    private String description;

    private String urlWeb;

    private String urlLogo;

    private String contacts;

    private List<LocationProfile> locations;
}
