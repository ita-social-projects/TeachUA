package com.softserve.teachua.dto.certificate_by_template;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateByTemplateSavingResponse {
    private List<String[]> messages;
    private List<Map<String, String>> invalidValues;
}
