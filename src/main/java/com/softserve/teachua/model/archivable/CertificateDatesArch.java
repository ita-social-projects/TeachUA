package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CertificateDatesServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
public class CertificateDatesArch implements Convertible, Archivable {
    private String dates;
    private Integer hours;
    private String duration;
    private String courseNumber;
    private String studyForm;

    @Override
    public Class<CertificateDatesServiceImpl> getServiceClass() {
        return CertificateDatesServiceImpl.class;
    }
}
