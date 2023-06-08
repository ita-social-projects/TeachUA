package com.softserve.teachua.model.archivable;

import com.softserve.commons.marker.Archivable;
import com.softserve.teachua.service.impl.LocationServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class LocationArch implements Archivable {
    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String phone;
    private Long districtId;
    private Long stationId;
    private Long cityId;
    private Long clubId;
    private Long centerId;

    @Override
    public Class<LocationServiceImpl> getServiceClass() {
        return LocationServiceImpl.class;
    }
}
