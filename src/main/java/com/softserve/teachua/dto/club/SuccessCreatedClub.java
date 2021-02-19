package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.District;
import com.softserve.teachua.model.Station;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedClub implements Convertible {
    private Long id;
    private String name;
    private City city;
    private String address;
    private Double latitude;
    private Double longitude;
    private Set<Category> categories;
    private String description;
    private Integer ageFrom;
    private Integer ageTo;
    private String urlWeb;
    private String urlLogo;
    private Double rating;
    private District district;
    private Station station;
    private Boolean isApproved;
}
