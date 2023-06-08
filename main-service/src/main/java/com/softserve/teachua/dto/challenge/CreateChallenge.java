package com.softserve.teachua.dto.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.utils.deserializers.HtmlModifyDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.commons.util.validation.CheckForeignLanguage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class CreateChallenge {
    @NotBlank
    @JsonDeserialize(using = TrimDeserialize.class)
    @Size(min = 5, max = 30, message = " must contain a minimum of 5 and a maximum of 30 letters")
    @CheckForeignLanguage
    private String name;
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @CheckForeignLanguage
    @Size(min = 5, max = 100, message = "must contain a minimum of 5 and a maximum of 100 letters")
    private String title;
    @NotBlank
    @Size(min = 40, max = 25000, message = "must contain a maximum of 25000 letters")
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @CheckForeignLanguage
    private String description;
    @JsonDeserialize(using = TrimDeserialize.class)
    @Pattern(regexp = "^https://docs\\.google\\.com/forms/d/e/[A-z0-9_-]+/viewform\\?embedded=true$",
            message = "must match https://docs.google.com/forms/d/e/{formCode}/viewform?embedded=true")
    @Size(max = 130, message = "must contain a maximum of 130 letters")
    private String registrationLink;
    @NotBlank
    @Pattern(regexp = "/upload/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path. It must be like /upload/*/*.png")
    private String picture;
    @NotNull
    private Long sortNumber;
}
