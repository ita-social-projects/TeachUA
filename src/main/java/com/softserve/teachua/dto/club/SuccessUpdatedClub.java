package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.center.CenterForClub;
import com.softserve.teachua.dto.contact_data.ContactDataResponse;
import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.GalleryPhoto;
import java.util.List;
import java.util.Set;
import com.softserve.teachua.model.WorkTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessUpdatedClub implements Convertible {
    private Long id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String urlBackground;
    private List<GalleryPhoto> urlGallery;
    private Set<WorkTime> workTimes;
    private Set<Category> categories;
    private UserPreview user;
    private CenterForClub center;
    private Double rating;
    private Set<LocationProfile> locations;
    private Boolean isApproved;
    private Boolean isOnline;
    private Long feedbackCount;
    private Set<ContactDataResponse> contacts;
}
