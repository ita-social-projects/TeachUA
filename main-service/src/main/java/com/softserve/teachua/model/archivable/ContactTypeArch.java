package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.clients.marker.Archivable;
import com.softserve.teachua.service.impl.ContactTypeServiceImpl;
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
public class ContactTypeArch implements Convertible, Archivable {
    private String name;
    private String urlLogo;

    @Override
    public Class<ContactTypeServiceImpl> getServiceClass() {
        return ContactTypeServiceImpl.class;
    }
}
