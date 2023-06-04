package com.softserve.certificate.dto.certificate_template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateTemplateMetadataTransfer {
    private String templateLastModifiedDate;
    private String templateName;
}
