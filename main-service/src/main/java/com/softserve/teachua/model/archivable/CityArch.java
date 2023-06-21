package com.softserve.teachua.model.archivable;

import com.softserve.commons.util.marker.Convertible;
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
public class CityArch implements Convertible/*, Archivable*/ {
    private String name;
    private Double latitude;
    private Double longitude;

    //todo@
    /*
    @Override
    public Class<CityServiceImpl> getServiceClass() {
        return CityServiceImpl.class;
    }
    */
}
