package com.softserve.teachua.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class ClubProfile {
    @NotNull
    private int ageFrom;

    @NotNull
    private int ageTo;

    @NotEmpty
    private String name;
}
