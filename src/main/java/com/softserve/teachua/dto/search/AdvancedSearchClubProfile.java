package com.softserve.teachua.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.List;

@AllArgsConstructor
@Data
public class AdvancedSearchClubProfile {
    private Integer ageFrom;
    private Integer ageTo;

    @Nullable
    private String districtName;

    @Nullable
    private String cityName;

    @Nullable
    private String stationName;

    @Nullable
    private List<String> categoriesName;

    private Boolean isOnline;

    private boolean isCenter;
}
