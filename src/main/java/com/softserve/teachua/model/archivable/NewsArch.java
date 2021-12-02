package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.NewsServiceImpl;
import lombok.*;

import java.time.LocalDate;

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
    public Class getServiceClass() {
        return NewsServiceImpl.class;
    }
}
