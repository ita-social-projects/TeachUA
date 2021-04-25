package com.softserve.teachua.dto.category;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedCategory implements Convertible {

    private Integer sortby;

    private String name;

    private String description;

    private String urlLogo;

    private String backgroundColor;

    private String tagBackgroundColor;

    private String tagTextColor;
}