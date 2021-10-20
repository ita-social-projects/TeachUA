package com.softserve.teachua.dto.location;

import com.softserve.teachua.dto.city.CityResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LocationResponse {

    private Long id;
    private String name;

    private String address;

    private CityResponse city;
    private String districtName;
    private String stationName;

    private String coordinates;
    private Double longitude;
    private Double latitude;


}
