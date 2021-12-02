package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CityServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class CityArch implements Convertible, Archivable {
    private String name;
    private Double latitude;
    private Double longitude;

    @Override
    public Class getServiceClass() {
        return CityServiceImpl.class;
    }
}