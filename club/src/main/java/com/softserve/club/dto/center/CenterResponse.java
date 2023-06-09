package com.softserve.club.dto.center;

import com.softserve.club.dto.club.ClubResponse;
import com.softserve.club.dto.contact_data.ContactDataResponse;
import com.softserve.club.dto.location.LocationProfile;
import com.softserve.commons.user.UserPreview;
import com.softserve.commons.util.marker.Convertible;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CenterResponse implements Convertible {
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

    private Set<ClubResponse> clubs;

    private Set<ContactDataResponse> contacts;
}
