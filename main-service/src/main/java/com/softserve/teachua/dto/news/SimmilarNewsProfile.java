package com.softserve.teachua.dto.news;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SimmilarNewsProfile implements Convertible {
    private Long id;

    private String title;
}
