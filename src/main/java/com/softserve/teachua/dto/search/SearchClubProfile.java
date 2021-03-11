package com.softserve.teachua.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SearchClubProfile {
    private String clubName;
    private String cityName;
    private String categoryName;
}
