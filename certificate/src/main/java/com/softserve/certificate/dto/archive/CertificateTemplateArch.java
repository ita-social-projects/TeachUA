package com.softserve.certificate.dto.archive;

import com.softserve.commons.util.marker.Convertible;
import com.softserve.certificate.service.impl.CertificateTemplateServiceImpl;
import com.softserve.commons.util.marker.Archivable;
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
public class CertificateTemplateArch implements Convertible, Archivable {
    private String name;
    private String filePath;

    @Override
    public Class<CertificateTemplateServiceImpl> getServiceClass() {
        return CertificateTemplateServiceImpl.class;
    }
}
