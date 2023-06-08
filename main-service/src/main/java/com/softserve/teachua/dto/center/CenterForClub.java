package com.softserve.teachua.dto.center;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.commons.user.UserPreview;
import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.dto.location.LocationProfile;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CenterForClub {
    private Long id;

    private String name;

    private String urlBackgroundPicture;

    private String email;

    private String phones;

    private String description;

    private String urlWeb;

    private String urlLogo;

    private String socialLinks;

    private UserPreview user;

    private Set<LocationProfile> locations;

    private Set<ContactDataResponse> contacts;
}
