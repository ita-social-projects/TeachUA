package com.softserve.teachua.dto.center;

import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    private User user;

    private Set<Location> locations;

    private Set<Club> clubs;

    private Set<ContactDataResponse> contacts;
}

