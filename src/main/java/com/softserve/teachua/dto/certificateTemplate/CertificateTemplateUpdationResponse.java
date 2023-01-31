package com.softserve.teachua.dto.certificateTemplate;

import com.softserve.teachua.model.CertificateTemplate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateTemplateUpdationResponse {
    boolean isUpdated;
    List<String> messages;
    CertificateTemplate template;
}
