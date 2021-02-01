package com.softserve.teachua.dto.service;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
public class CategoryProfile implements Dto {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String urlLogo;
}
