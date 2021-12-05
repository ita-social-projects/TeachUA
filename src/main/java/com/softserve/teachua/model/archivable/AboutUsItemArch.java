package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.AboutUsItemServiceImpl;
import lombok.*;

import javax.persistence.*;

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
    public Class getServiceClass() {
        return AboutUsItemServiceImpl.class;
    }
}
