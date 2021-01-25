package com.softserve.teachua.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
public class CategoryProfile {
    @NotEmpty
    private String name;

    @NotEmpty
    private String urlLogo;
}
