package com.softserve.teachua.dto.certificate_type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softserve.teachua.constants.MessageType;
import com.softserve.teachua.model.CertificateType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateTypeProcessingResponse {
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    List<Pair<String, MessageType>> messages;
    CertificateType certificateType;
}
