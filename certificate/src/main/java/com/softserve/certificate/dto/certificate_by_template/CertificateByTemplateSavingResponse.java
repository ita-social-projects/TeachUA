package com.softserve.certificate.dto.certificate_by_template;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softserve.certificate.constants.MessageType;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateByTemplateSavingResponse {
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private List<Pair<String, MessageType>> messages;
    private List<Map<String, String>> invalidValues;
}
