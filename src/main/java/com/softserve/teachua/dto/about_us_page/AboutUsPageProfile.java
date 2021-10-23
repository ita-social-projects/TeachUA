package com.softserve.teachua.dto.about_us_page;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AboutUsPageProfile implements Convertible {

    @NotNull
    @NotEmpty
    private String topic;

    @NotNull
    @NotEmpty
    private String tittle;

    @NotNull
    @NotEmpty
    private String subtitle;

    @NotNull
    private String picture;

}
