package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.City;
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

    @NotEmpty
    private String phones;

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

    private String email;

    private String urlWeb;

    private String urlLogo;

    private String socialLinks;

    private Double rating;

}
