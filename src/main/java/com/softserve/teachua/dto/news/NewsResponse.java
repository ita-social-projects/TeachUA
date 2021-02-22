package com.softserve.teachua.dto.news;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
;import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsResponse implements Convertible {

    private Long id;

    private String title;

    private String description;

    private String urlTitleLogo;

    private Timestamp date;
}
