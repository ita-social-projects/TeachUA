package com.softserve.teachua.dto.district;

import com.softserve.teachua.dto.marker.Convertible;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DistrictProfile implements Convertible {
    @NotEmpty
    private String name;

    @NotEmpty
    private String cityName;
}
