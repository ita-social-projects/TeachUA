package com.softserve.club.dto.city;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedCity implements Convertible {
    private Long id;
    private String name;
    private Double longitude;
    private Double latitude;
}
