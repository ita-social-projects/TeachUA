package com.softserve.teachua.dto.news;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;


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
