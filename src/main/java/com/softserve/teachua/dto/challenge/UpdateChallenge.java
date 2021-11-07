package com.softserve.teachua.dto.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.task.TaskPreview;
import com.softserve.teachua.utils.deserializers.HtmlModifyDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import lombok.*;

import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateChallenge implements Convertible {
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()!\"#$%&'*+ ,-.:;<=>?|@_`{}~\\\\^&&[^ыЫъЪёЁэЭ]]+$",
            message = "can contains only letters of ukrainian and english languages, numbers, and some special symbols like: [\"#$%&'*+ ,-.:;<=>?|@_`{}~]")
    @Size(min = 5, max = 30, message = "Name must contain a minimum of 5 and a maximum of 30 letters")
    private String name;
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()!\"#$%&'*+ ,-.:;<=>?|@_`{}~^&&[^ыЫъЪёЁэЭ]]+$",
            message = "can contains only letters of ukrainian and english languages, numbers, and some special symbols like: [\"#$%&'*+ ,-.:;<=>?|@_`{}~]")
    @Size(min = 5, max = 50, message = "must contain a minimum of 5 and a maximum of 50 letters")
    private String title;
    @JsonDeserialize(using = HtmlModifyDeserialize.class)
    @NotBlank
    @Pattern(regexp = "^[^ыЫъЪёЁэЭ]+$", message = "can not contain letters of russian languages")
    @Size(min = 40, max = 3000, message = "must contain a minimum of 40 and a maximum of 3000 letters")
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
    @NotNull
    private Boolean isActive;
    @NotNull
    @ToString.Exclude
    private Set<TaskPreview> tasks;
}
