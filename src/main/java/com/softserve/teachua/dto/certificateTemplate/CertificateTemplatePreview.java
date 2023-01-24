package com.softserve.teachua.dto.certificateTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.marker.Convertible;
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
    private Long id;
    private String name;
    private String filePath;
    private String courseDescription;
    private String projectDescription;
}
