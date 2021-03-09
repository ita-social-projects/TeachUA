package com.softserve.teachua.dto.club;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ClubProfile implements Convertible {
    private Long id;

    @NotEmpty
    private String cityName;

    @NotEmpty
    private String address;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotEmpty
    private List<String> categoriesName;

    private String description;

    @NotNull
    private String name;

    @NotNull
    private Integer ageFrom;

    @NotNull
    private Integer ageTo;

    private String urlBackground;

    private String urlLogo;

    private String stationName;

    private String districtName;

   // private Center center;
}
