package com.softserve.teachua.repository.utils.club_test_data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ClubSearchTestEntity {
    private Integer age;
    private String cityName;
    private String districtName;
    private String stationName;
    private List<String> categoriesNames;
    private boolean isOnline;
    private Integer pageNumber;
}
