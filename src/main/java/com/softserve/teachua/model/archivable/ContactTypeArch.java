package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.ContactTypeServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class ContactTypeArch implements Convertible, Archivable {
    private String name;
    private String urlLogo;

    @Override
    public Class getServiceClass() {
        return ContactTypeServiceImpl.class;
    }
}
