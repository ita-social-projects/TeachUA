package com.softserve.teachua.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.deserializers.HtmlModifyDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.validations.CheckRussian;
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
public class UpdateTask implements Convertible {
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @CheckRussian
    @Size(min = 5, max = 30, message = "must contain a minimum of 5 and a maximum of 30 letters")
    private String name;
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @NotBlank
    @CheckRussian
    @Size(min = 40, max = 3000, message = "must contain a minimum of 40 and a maximum of 3000 letters")
    private String headerText;
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @CheckRussian
    @Size(max = 3000, message = "must contain a maximum of 3000 letters")
    private String description;
    @NotBlank
    @Pattern(regexp = "/upload/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path. It must be like /upload/*/*.png")
    private String picture;
    @NotNull
    @Future
    private LocalDate startDate;
    @NotNull
    private Long challengeId;
}
