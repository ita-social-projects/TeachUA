package com.softserve.teachua.dto.certificateType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)

public class CertificateTypeProfile {
    private Integer id;
    @Min(value = 1, message = "(Кодовий номер) занадто малий")
    private Integer codeNumber;
    @NotBlank(message = "(Назва типу) не може бути пустою")
    @Size(max = 250, message = "(Назва типу) занадто велика")
    private String name;
}
