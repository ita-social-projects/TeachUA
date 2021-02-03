package com.softserve.teachua.dto.search;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SearchPossibleResponse implements Convertible {
    private Long id;
    private String name;
}
