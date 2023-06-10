package com.softserve.teachua.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.commons.util.marker.Convertible;
import com.softserve.commons.util.validation.CheckForeignLanguage;
import com.softserve.teachua.utils.deserializers.HtmlModifyDeserialize;
import com.softserve.commons.util.deserializer.TrimDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateTask implements Convertible {
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @CheckForeignLanguage
    @Size(min = 5, max = 50, message = "must contain a minimum of 5 and a maximum of 50 letters")
    private String name;
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @NotBlank
    @CheckForeignLanguage
    @Size(min = 40, max = 10000, message = "must contain a minimum of 40 and a maximum of 10000 letters")
    private String headerText;
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @CheckForeignLanguage
    @NotBlank(message = "must not be blank")
    @Size(min = 40, max = 3000, message = "must contain a minimum of 40 and a maximum of 3000 letters")
    private String description;
    @NotBlank
    @Pattern(regexp = "/upload/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path. It must be like /upload/*/*.png")
    private String picture;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private Long challengeId;
}
