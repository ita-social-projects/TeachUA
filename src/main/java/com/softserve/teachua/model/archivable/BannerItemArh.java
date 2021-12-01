package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.BannerItemServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class BannerItemArh implements Archivable, Convertible {
    private String title;
    private String subtitle;
    private String link;
    private String picture;
    private Integer sequenceNumber;

    @Override
    public Class getServiceClass() {
        return BannerItemServiceImpl.class;
    }
}
