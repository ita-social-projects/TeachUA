package com.softserve.teachua.dto.certificateTemplate;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CertificateTemplateProfile implements Convertible {
    private Integer id;

    @NotBlank
    private String name;

    @NotBlank
    private String filePath;

    private Integer certificateType;
}
