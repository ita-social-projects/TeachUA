package com.softserve.certificate.dto.archive;

import com.softserve.certificate.service.impl.CertificateDatesServiceImpl;
import com.softserve.commons.marker.Archivable;
import com.softserve.commons.marker.Convertible;
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
