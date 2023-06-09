package com.softserve.club.dto.station;

import com.softserve.commons.util.marker.Convertible;
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

    private String cityName;

    private String districtName;
}
