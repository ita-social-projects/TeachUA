package com.softserve.teachua.dto.search;

import com.softserve.teachua.dto.marker.Dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchPossibleResponse implements Dto {
    private Long id;
    private String name;
}
