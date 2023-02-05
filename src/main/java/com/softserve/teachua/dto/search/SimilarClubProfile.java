package com.softserve.teachua.dto.search;

import java.util.List;
import lombok.Data;

@Data
public class SimilarClubProfile {
    private Long id;
    private List<String> categoriesName;
    private String cityName;
}
