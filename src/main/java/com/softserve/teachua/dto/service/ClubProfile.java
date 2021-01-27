package com.softserve.teachua.dto.service;

import com.softserve.teachua.model.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Data
public class ClubProfile {

    @NotNull
    private Integer ageFrom;

    @NotNull
    private Integer ageTo;

    @NotEmpty
    private String name;
}
