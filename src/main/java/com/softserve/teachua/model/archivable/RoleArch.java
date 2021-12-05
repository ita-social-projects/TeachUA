package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.RoleServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class RoleArch implements Convertible, Archivable {
    private String name;

    @Override
    public Class getServiceClass() {
        return RoleServiceImpl.class;
    }
}
