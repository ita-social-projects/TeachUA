package com.softserve.teachua.dto.about_us_item;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AboutUsItemProfile implements Convertible {
    @CheckRussian
    @Size(min = 2, max = 6000, message = "must contain a minimum of 2 and a maximum of 6000 letters")
    private String text;

    private String picture;

    @Pattern(regexp = "(https\\:\\/\\/www\\.youtube\\.com\\/+watch\\?v\\=)(.*)",
            message = "must match https://www.youtube.com/watch?v=")
    private String video;

    @NotNull
    @Min(value = 1, message = "type must be equals or between 1 and 5")
    @Max(value = 5, message = "type must be equals or between 1 and 5")
    private Long type;

    private Long number;
}
