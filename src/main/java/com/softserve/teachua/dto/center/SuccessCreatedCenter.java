package com.softserve.teachua.dto.center;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedCenter implements Convertible {

    private Long id;

    private String name;

    private String email;

    private String phones;

    private String description;

    private String urlWeb;

    private String urlLogo;

    private String contacts;

    private List<LocationProfile> locations;

}
