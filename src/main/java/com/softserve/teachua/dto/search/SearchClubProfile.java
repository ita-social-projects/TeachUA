package com.softserve.teachua.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SearchClubProfile {
    private String clubName = "";
    private String cityName = "";
    private String districtName = "";
    private String stationName = "";
    private String categoryName = "";
}
