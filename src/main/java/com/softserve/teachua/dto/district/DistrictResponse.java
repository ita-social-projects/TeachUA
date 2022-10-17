package com.softserve.teachua.dto.district;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DistrictResponse implements Convertible {
    private Long id;

    private String name;

    private String cityName;
}
