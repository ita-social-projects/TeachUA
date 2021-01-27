package com.softserve.teachua.dto.service;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@Builder
@Data
public class ClubProfile implements Dto {

    @NotEmpty
    private String name;

    @NotNull
    private City city;

    @NotEmpty
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotEmpty
    private String phones;

    @NotNull
    private Set<Category> categories;

    @NotEmpty
    private String description;

    @NotNull
    private Integer ageFrom;

    @NotNull
    private Integer ageTo;

    private String email;

    private String urlWeb;

    private String urlLogo;

    private String socialLinks;
}
