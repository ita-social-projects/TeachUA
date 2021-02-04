package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String email;
    private String address;
    private String phones;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String workTime;
    private String socialLinks;
    private Double latitude;
    private Double longitude;
    private City city;
    private Set<Category> categories;
    private User user;
    private Center center;
    private Double rating;
    private District district;
    private Station station;
}
