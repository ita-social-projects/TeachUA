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
public class StationArch implements Convertible/*, Archivable*/ {
    private String name;
    private Long cityId;

    //todo
    /*
    @Override
    public Class<StationServiceImpl> getServiceClass() {
        return StationServiceImpl.class;
    }
    */
}
