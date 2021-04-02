package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.location.LocationProfile;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.Location;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedClub implements Convertible {
    private Long id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String urlBackground;
    private String workTime;
    private Set<Category> categories;
    private User user;
    private Center center;
    private Double rating;
    private Set<Location> locations;
    private Boolean isApproved;
    private Boolean isOnline;
}
