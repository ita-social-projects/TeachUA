package com.softserve.teachua.dto.certificateType;

import com.softserve.teachua.model.CertificateType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateTypeProcessingResponse {
    List<String[]> messages;
    CertificateType certificateType;
}
