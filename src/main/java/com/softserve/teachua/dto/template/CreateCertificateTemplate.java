package com.softserve.teachua.dto.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.utils.validations.CheckForeignLanguage;
import javax.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCertificateTemplate {

    @NotBlank
    @CheckForeignLanguage
    private String name;
    @NotBlank
    @CheckForeignLanguage
    private String courseDescription;
    @NotBlank
    @CheckForeignLanguage
    private String projectDescription;
    @NotBlank
    private String filePath;
    @NotBlank
    private String certificateType;
    private String properties;

}
