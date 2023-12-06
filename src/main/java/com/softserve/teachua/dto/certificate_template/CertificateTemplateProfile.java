package com.softserve.teachua.dto.certificate_template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class CertificateTemplateProfile {
    private Integer id;
    @Size(max = 250, message = "(Назва шаблону) містить занадто велике значення!")
    private String name;
    private String filePath;
    private Integer certificateType;
    @Size(max = 1020, message = "(Опис курсу) містить занадто великий опис!")
    private String courseDescription;
    @Size(max = 1020, message = "(Опис проекту) містить занадто великий опис!")
    private String projectDescription;
    private String picturePath;
    private String properties;
}
