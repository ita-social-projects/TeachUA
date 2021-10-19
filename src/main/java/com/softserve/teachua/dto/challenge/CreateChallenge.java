package com.softserve.teachua.dto.challenge;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateChallenge {
    @NotNull
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()!\"#$%&'*+ ,-.:;<=>?|@_`{}~^&&[^ыЫъЪёЁэЭ]]+$",
            message = "can contains only letters of ukrainian and english languages, numbers, and some special symbols like: [\"#$%&'*+ ,-.:;<=>?|@_`{}~]")
    @Size(min = 5, max = 30, message = "Name must contain a minimum of 5 and a maximum of 30 letters")
    private String name;
    @NotNull
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()!\"#$%&'*+ ,-.:;<=>?|@_`{}~^&&[^ыЫъЪёЁэЭ]]+$",
            message = "can contains only letters of ukrainian and english languages, numbers, and some special symbols like: [\"#$%&'*+ ,-.:;<=>?|@_`{}~]")
    @Size(min = 5, max = 30, message = "Title must contain a minimum of 5 and a maximum of 30 letters")
    private String title;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[^ыЫъЪёЁэЭ]+$", message = "can not contain letters of russian languages")
    @Size(min = 40, max = 3000, message = "must contain a minimum of 40 and a maximum of 3000 letters")
    private String description;
    @NotNull
    @Pattern(regexp = "/upload/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path. It must be like /upload/*/*.png")
    private String picture;
    @NotNull
    private Long sortId;
}
