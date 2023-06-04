package com.softserve.certificate.dto.certificate_excel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softserve.certificate.constants.MessageType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateByTemplateExcelValidationResult {
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private List<Pair<String, MessageType>> messages;
}
