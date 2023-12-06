package com.softserve.teachua.dto.search;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@Data
public class AdvancedSearchClubProfile {
    @Nullable
    private String name;

    private Integer age;

    @Nullable
    private String districtName;

    @Nullable
    private String cityName;

    @Nullable
    private String stationName;

    @Nullable
    private List<String> categoriesName;

    private Boolean isOnline;
}
