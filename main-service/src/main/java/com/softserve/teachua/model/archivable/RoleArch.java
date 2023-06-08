package com.softserve.teachua.model.archivable;

import com.softserve.commons.util.marker.Convertible;
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
public class RoleArch implements Convertible/*, Archivable*/ {
    private String name;
    //todo
    //@Override
    //public Class<RoleServiceImpl> getServiceClass() {
    //    return RoleServiceImpl.class;
    //}
}
