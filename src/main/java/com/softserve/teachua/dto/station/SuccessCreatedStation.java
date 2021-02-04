package com.softserve.teachua.dto.station;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.District;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedStation implements Convertible {

    private Long id;

    private String name;

    private District district;

    private City city;
}
