package com.softserve.teachua.dto.category;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryProfile implements Convertible {

    private Long id;

    private Integer sortby;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String urlLogo;

    @NotEmpty
    private String backgroundColor;

    @NotEmpty
    private String tagBackgroundColor;

    @NotEmpty
    private String tagTextColor;
}