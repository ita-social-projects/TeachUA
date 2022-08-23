package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CertificateTemplateServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class CertificateTemplateArch implements Convertible, Archivable {

    private String name;
    private String filePath;

    @Override
    public Class getServiceClass() {
        return CertificateTemplateServiceImpl.class;
    }
}