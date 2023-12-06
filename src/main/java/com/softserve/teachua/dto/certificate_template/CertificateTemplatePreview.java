package com.softserve.teachua.dto.certificate_template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.CertificateType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateTemplatePreview implements Convertible {
    private Integer id;
    private boolean isUsed;
    private String name;
    private String filePath;
    private CertificateType certificateType;
    private String courseDescription;
    private String projectDescription;
    private String picturePath;
    private String properties;
}
