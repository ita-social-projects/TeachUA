package com.softserve.teachua.dto.certificateByTemplate;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CertificateTemplateUploadResponse {
    private List<String> fieldsList;
    private String templateName;
}
