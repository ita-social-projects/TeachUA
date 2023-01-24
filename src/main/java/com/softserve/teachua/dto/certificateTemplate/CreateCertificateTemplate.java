package com.softserve.teachua.dto.certificateTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class CreateCertificateTemplate {
    @Size(max = 250, message = "(Назва шаблону) містить занадто велике значення!")
    private String name;
    @Size(max = 1020, message = "(Опис курсу) містить занадто великий опис!")
    private String courseDescription;
    @Size(max = 1020, message = "(Опис проекту) містить занадто великий опис!")
    private String projectDescription;
    private String filePath;
    private String certificateType;
    private String properties;
}
