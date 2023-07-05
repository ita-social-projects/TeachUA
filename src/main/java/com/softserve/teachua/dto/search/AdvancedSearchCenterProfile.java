package com.softserve.teachua.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdvancedSearchCenterProfile {
    @Nullable
    private String centerName;

    @Nullable
    private String districtName;

    @Nullable
    private String cityName;

    @Nullable
    private String stationName;
}
