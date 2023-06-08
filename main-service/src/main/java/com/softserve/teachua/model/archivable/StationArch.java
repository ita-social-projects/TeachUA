package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.commons.marker.Archivable;
import com.softserve.teachua.service.impl.StationServiceImpl;
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
public class StationArch implements Convertible, Archivable {
    private String name;
    private Long cityId;

    @Override
    public Class<StationServiceImpl> getServiceClass() {
        return StationServiceImpl.class;
    }
}
