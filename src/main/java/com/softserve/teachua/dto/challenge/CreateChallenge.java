package com.softserve.teachua.dto.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.utils.deserializers.HtmlModifyDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateChallenge {
    @NotBlank
    @JsonDeserialize(using = TrimDeserialize.class)
    @Size(min = 5, max = 30, message = " must contain a minimum of 5 and a maximum of 30 letters")
    @CheckRussian(message = "can`t contain russian letters")
    private String name;
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @CheckRussian
    @Size(min = 5, max = 50, message = "must contain a minimum of 5 and a maximum of 50 letters")
    private String title;
    @NotBlank
    @Size(max = 3000, message = "must contain a maximum of 3000 letters")
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @CheckRussian
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
