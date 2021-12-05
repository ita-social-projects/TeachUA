package com.softserve.teachua.repository.utils.club_test_data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ClubSearchTestEntity {
    private Integer age;
    private String cityName;
    private String districtName;
    private String stationName;
    private String categoriesName;
    private boolean isOnline;
    private Integer pageNumber;
}
