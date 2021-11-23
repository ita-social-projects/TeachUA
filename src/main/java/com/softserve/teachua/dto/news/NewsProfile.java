package com.softserve.teachua.dto.news;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsProfile implements Convertible {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    private String urlTitleLogo;

    private Boolean isActive;

}
