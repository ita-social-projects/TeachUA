package com.softserve.certificate.dto.archive;

import com.softserve.certificate.service.impl.CertificateServiceImpl;
import com.softserve.clients.marker.Archivable;
import com.softserve.clients.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Data
public class CertificateArch implements Archivable, Convertible {
    private Long serialNumber;
    private Long userId;
    private String userName;
    private String userEmail;
    private boolean sendStatus;
    private LocalDate updateStatus;
    private Integer templateId;
    private Integer datesId;
    private String values;

    @Override
    public Class<CertificateServiceImpl> getServiceClass() {
        return CertificateServiceImpl.class;
    }
}
