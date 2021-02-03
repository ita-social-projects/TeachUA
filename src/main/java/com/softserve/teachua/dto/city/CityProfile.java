package com.softserve.teachua.dto.city;

import com.softserve.teachua.dto.marker.Convertible;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CityProfile implements Convertible {

    private Long id;

    @NotEmpty
    private String name;
}
