package com.softserve.teachua.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LocationProfile {
    private String name;
    private String address;
    private String cityName;
    private String districtName;
    private String stationName;
    private Double longitude;
    private Double latitude;
    private String phone;
    private Double key;
}
