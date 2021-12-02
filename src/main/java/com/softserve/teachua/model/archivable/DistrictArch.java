package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.marker.Archivable;
import lombok.*;

import javax.persistence.*;

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
        return DistrictArch.class;
    }
}
