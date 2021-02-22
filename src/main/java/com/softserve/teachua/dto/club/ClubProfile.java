package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClubProfile implements Convertible {

    private Long id;

    @NotNull
    private City city;

    @NotEmpty
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Set<Category> categories;

    @NotEmpty
    private String description;

    @NotNull
    private String name;

    @NotNull
    private Integer ageFrom;

    @NotNull
    private Integer ageTo;

    @NotNull
    private Boolean isApproved;

    private String urlWeb;

    private String urlLogo;

    private Double rating;

    private Station station;

    private District district;

    private Center center;


}
