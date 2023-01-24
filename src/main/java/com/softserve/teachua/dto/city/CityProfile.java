package com.softserve.teachua.dto.city;

import com.softserve.teachua.dto.marker.Convertible;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
