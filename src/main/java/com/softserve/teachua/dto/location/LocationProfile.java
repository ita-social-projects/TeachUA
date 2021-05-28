package com.softserve.teachua.dto.location;

import com.softserve.teachua.model.Club;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
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
