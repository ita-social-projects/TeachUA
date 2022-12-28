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
public class SuccessCreatedCertificateTemplate implements Convertible {

    private Integer id;
    private String name;
    private Integer certificateType;
    private String filePath;
    private String courseDescription;
    private String projectDescription;
    private String picturePath;
    private String properties;

}
