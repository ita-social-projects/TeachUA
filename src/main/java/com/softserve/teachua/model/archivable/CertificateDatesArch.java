package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CertificateDatesServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class CertificateDatesArch implements Convertible, Archivable {

    private String dates;
    private Integer hours;
    private String duration;

    @Override
    public Class getServiceClass() {
        return CertificateDatesServiceImpl.class;
    }
}
