package com.softserve.teachua.dto.certificateTemplate;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateTemplateCreationResponse {

    boolean isValid;
    List<String> messages;
    SuccessCreatedCertificateTemplate template;

}
