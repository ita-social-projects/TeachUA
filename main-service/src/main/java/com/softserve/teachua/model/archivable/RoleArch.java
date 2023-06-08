package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.commons.marker.Archivable;
import com.softserve.teachua.service.impl.RoleServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class RoleArch implements Convertible, Archivable {
    private String name;

    @Override
    public Class<RoleServiceImpl> getServiceClass() {
        return RoleServiceImpl.class;
    }
}
