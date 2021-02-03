package com.softserve.teachua.dto.category;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryProfile implements Convertible {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String urlLogo;

    @NotEmpty
    private String backgroundColor;
}
