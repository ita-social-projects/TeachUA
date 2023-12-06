package com.softserve.teachua.dto.search;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CombinedPossibleResponse {
    private List<SearchPossibleResponse> categories;
    private List<SearchPossibleResponse> clubs;
}
