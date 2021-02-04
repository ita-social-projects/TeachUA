package com.softserve.teachua.dto.station;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StationProfile implements Convertible {

    @NotEmpty
    private String name;

    @NotNull
    private District district;

    @NotNull
    private City city;
}
