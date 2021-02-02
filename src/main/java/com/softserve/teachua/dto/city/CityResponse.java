package com.softserve.teachua.dto.city;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CityResponse implements Dto {
    private Long id;
    private String name;
}
