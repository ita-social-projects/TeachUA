package com.softserve.teachua.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
public class SimilarClubProfile {
    private Long id;
    private String categoryName;
    private String cityName;
}
