package com.softserve.teachua.dto.news;

import com.softserve.commons.user.UserPreview;
import com.softserve.commons.util.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsResponse implements Convertible {
    private Long id;

    private String title;

    private String description;

    private String urlTitleLogo;

    private LocalDate date;

    private Boolean isActive;

    private UserPreview user;
}
