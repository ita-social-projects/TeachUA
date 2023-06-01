package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.BannerItemServiceImpl;
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
public class BannerItemArch implements Archivable, Convertible {
    private String title;
    private String subtitle;
    private String link;
    private String picture;
    private Integer sequenceNumber;

    @Override
    public Class<BannerItemServiceImpl> getServiceClass() {
        return BannerItemServiceImpl.class;
    }
}
