package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.commons.marker.Archivable;
import com.softserve.teachua.service.impl.CityServiceImpl;
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
public class CityArch implements Convertible, Archivable {
    private String name;
    private Double latitude;
    private Double longitude;

    @Override
    public Class<CityServiceImpl> getServiceClass() {
        return CityServiceImpl.class;
    }
}
