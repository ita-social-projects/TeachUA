package com.softserve.teachua.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.deserializers.HtmlModifyDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.validations.CheckForeignLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTask implements Convertible {
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @CheckForeignLanguage
    @Size(min = 5, max = 100, message = "must contain a minimum of 5 and a maximum of 100 letters")
    private String name;
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @NotBlank
    @CheckForeignLanguage
    @Size(min = 40, max = 3000, message = "must contain a minimum of 40 and a maximum of 3000 letters")
    private String headerText;
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @CheckForeignLanguage
    @Size(min = 40, max = 3000, message = "must contain a minimum of 40 and a maximum of 3000 letters")
    private String description;
    @NotBlank
    @Pattern(regexp = "/upload/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path. It must be like /upload/*/*.png")
    private String picture;
    @NotNull
    @Future(message = "дата має бути в майбутньому")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
}
