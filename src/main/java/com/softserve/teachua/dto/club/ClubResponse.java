package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.category.CategoryResponse;
import com.softserve.teachua.dto.center.CenterForClub;
import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.dto.location.LocationResponse;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClubResponse implements Convertible {
    private Long id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String urlBackground;
    private List<GalleryPhoto> urlGallery;
    private String workTime;
    private Set<CategoryResponse> categories;
    private Long userId;
    private CenterForClub club;
    private Double rating;
    private Set<LocationResponse> locations;
    private Boolean isApproved;
    private Boolean isOnline;

    private Set<ContactDataResponse> contacts;
}
