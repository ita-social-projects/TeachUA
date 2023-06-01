package com.softserve.teachua.dto.certificate_type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
