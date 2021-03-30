package com.softserve.teachua.dto.category;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponse implements Convertible {

    private Long id;

    private String name;

    private String urlLogo;

    private String description;

    private String backgroundColor;

    private String tagBackgroundColor;

    private String tagTextColor;

}
