package com.softserve.teachua.dto.about_us_item;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AboutUsItemProfile implements Convertible {
    private String text;

    private String picture;

    private String video;

    @NotNull
    private Long type;

    private Long number;
}
