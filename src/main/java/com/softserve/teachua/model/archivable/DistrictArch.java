package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.DistrictServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class DistrictArch implements Convertible, Archivable {
    private String name;
    private Long cityId;

    @Override
    public Class getServiceClass() {
        return DistrictServiceImpl.class;
    }
}
