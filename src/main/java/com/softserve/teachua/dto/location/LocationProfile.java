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
    private Long id;
    private String name;
    private String address;
    private Long cityId;
    private Long districtId;
    private Long stationId;
    private Double longitude;
    private Double latitude;

    private Long centerId;
    private Long clubId;

}
