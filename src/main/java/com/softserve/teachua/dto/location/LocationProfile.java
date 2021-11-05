package com.softserve.teachua.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.club.validation.CheckRussian;
import com.softserve.teachua.model.Club;
import lombok.*;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationProfile {

    private Long id;

    @NotEmpty
    @Size(
            min=5,
            max=100,
            message = "length should be between 5 and 100 symbols"
    )
    @CheckRussian
    private String name;

    private String address;
    private Long cityId;
    private Long districtId;
    private Long stationId;

    @CheckRussian
    private String cityName;
    @CheckRussian
    private String districtName;
    @CheckRussian
    private String stationName;

    private String coordinates;
    private Double longitude;
    private Double latitude;

    private Long centerId;
    private Long clubId;

}
