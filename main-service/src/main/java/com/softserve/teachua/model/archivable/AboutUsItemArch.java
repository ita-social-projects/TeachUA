package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.AboutUsItemServiceImpl;
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
public class AboutUsItemArch implements Convertible, Archivable {
    private String text;
    private String picture;
    private String video;
    private Long type;
    private Long number;

    @Override
    public Class<AboutUsItemServiceImpl> getServiceClass() {
        return AboutUsItemServiceImpl.class;
    }
}
