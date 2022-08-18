package com.softserve.teachua.dto.search;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CombinedPossibleResponse {
    private List<SearchPossibleResponse> categories;
    private List<SearchPossibleResponse> clubs;
}
