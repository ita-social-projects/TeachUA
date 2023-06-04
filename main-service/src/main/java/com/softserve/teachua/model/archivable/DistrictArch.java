package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.clients.marker.Archivable;
import com.softserve.teachua.service.impl.DistrictServiceImpl;
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
public class DistrictArch implements Convertible, Archivable {
    private String name;
    private Long cityId;

    @Override
    public Class<DistrictServiceImpl> getServiceClass() {
        return DistrictServiceImpl.class;
    }
}
