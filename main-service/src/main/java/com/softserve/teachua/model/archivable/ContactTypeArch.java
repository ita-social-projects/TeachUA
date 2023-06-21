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
public class ContactTypeArch implements Convertible/*, Archivable*/ {
    private String name;
    private String urlLogo;

    //todo@
    /*
    @Override
    public Class<ContactTypeServiceImpl> getServiceClass() {
        return ContactTypeServiceImpl.class;
    }
    */
}
