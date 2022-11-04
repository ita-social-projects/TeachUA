package com.softserve.teachua.dto.certificateTemplate;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CertificateTemplateResponse implements Convertible {
    private Integer id;
    private String name;
    private String filePath;
    private Integer certificateType;
    private String courseDescription;
    private String projectDescription;
    private String picturePath;
}
