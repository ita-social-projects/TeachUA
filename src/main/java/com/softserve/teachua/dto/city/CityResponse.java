package com.softserve.teachua.dto.city;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CityResponse implements Convertible {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
}
