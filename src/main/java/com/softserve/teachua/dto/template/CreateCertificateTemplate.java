package com.softserve.teachua.dto.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
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
    @JsonDeserialize(using = TrimDeserialize.class)
//    @Size(min = 5, max = 30, message = " must contain a minimum of 5 and a maximum of 30 letters")
    @CheckForeignLanguage
    private String name;
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @CheckForeignLanguage
//    @Size(min = 40, max = 25000, message = "must contain a maximum of 25000 letters")
    private String courseDescription;
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @CheckForeignLanguage
//    @Size(min = 40, max = 25000, message = "must contain a maximum of 25000 letters")
    private String projectDescription;
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
//    @Size(min = 40, max = 25000, message = "must contain a maximum of 25000 letters")
    private String filePath;
    @JsonDeserialize(using = TrimDeserialize.class)
    private String certificateType;
    //    @NotBlank
//    @Pattern(regexp = "/upload/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path. It must be like /upload/*/*.pdf")
//    private String pdfFile;
    private String properties;
}
