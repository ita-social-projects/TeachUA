package com.softserve.teachua.model.archivable;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.softserve.teachua.model.*;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.LocationServiceImpl;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

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
    public Class getServiceClass() {
        return LocationServiceImpl.class;
    }
}
