package com.softserve.teachua.dto.banner_item;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BannerItemProfile implements Convertible {

    private Long id;

    @NotBlank
    @Size(min = 5, max = 150, message = "length must be from 5 to 150 characters")
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()!\"#\\n№$%&'*+ ,-./:;<=>?|@_`{}~^]*$",
            message = "can contains only letters of ukrainian and english languages, numbers, and some special symbols")
    @Pattern(regexp = "^[^ЁёЪъЫыЭэ]*$", message = "can`t contain russian letters")
    private String title;

    @Size(min = 5, max = 250, message = " length should be from 5 to 250 characters")
    @Pattern(regexp = "^[А-Яа-яЇїІіЄєҐґa-zA-Z0-9()!\"#\\n№$%&'*+ ,-./:;<=>?|@_`{}~^]*$",
            message = "can contains only letters of ukrainian and english languages, numbers, and some special symbols")
    @Pattern(regexp = "^[^ЁёЪъЫыЭэ]*$", message = " can`t contain russian letters")
    private String subtitle;

    private String link;

    @NotBlank
    @Pattern(regexp = "/upload/\\b.+/[^/]+\\.[A-z]+", message = "Incorrect file path. It must be like /upload/*/*.png")
    private String picture;

    @NotNull
    private Integer sequenceNumber;
}
