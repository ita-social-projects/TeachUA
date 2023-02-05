package com.softserve.teachua.dto.category;

import com.softserve.teachua.dto.marker.Convertible;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class CategoryProfile implements Convertible {
    private Long id;

    @NotNull
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
