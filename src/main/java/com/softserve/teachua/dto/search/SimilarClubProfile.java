package com.softserve.teachua.dto.search;

import lombok.Data;

import java.util.List;

@Data
public class SimilarClubProfile {
    private Long id;
    private List<String> categoriesName;
    private String cityName;
}
