package com.softserve.teachua.dto.certificateByTemplate;

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
