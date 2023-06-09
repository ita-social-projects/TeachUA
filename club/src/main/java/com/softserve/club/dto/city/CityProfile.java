package com.softserve.club.dto.city;

import com.softserve.commons.util.marker.Convertible;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CityProfile implements Convertible {
    private Long id;

    @NotEmpty
    private String name;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;
}
