package com.softserve.teachua.dto.district;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.City;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DistrictResponse implements Convertible {
    private Long id;

    private String name;

    private String cityName;
}
