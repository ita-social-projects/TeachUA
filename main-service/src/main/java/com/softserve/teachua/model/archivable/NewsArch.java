package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.NewsServiceImpl;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class NewsArch implements Convertible, Archivable {
    private String title;
    private LocalDate date;
    private String urlTitleLogo;
    private String description;
    private Boolean isActive;
    private Long userId;

    @Override
    public Class<NewsServiceImpl> getServiceClass() {
        return NewsServiceImpl.class;
    }
}
