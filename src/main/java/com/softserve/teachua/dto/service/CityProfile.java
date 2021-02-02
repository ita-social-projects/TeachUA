package com.softserve.teachua.dto.service;

import com.softserve.teachua.dto.marker.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Data
public class CityProfile implements Dto {

    private Long id;

    @NotEmpty
    private String name;
}
