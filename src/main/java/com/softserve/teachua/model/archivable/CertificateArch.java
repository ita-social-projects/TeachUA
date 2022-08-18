package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CertificateServiceImpl;
import lombok.*;

import java.time.LocalDate;

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
    private LocalDate certificateDate;
    private boolean sendStatus;
    private LocalDate updateStatus;
    private Integer templateId;

    @Override
    public Class getServiceClass() {
        return CertificateServiceImpl.class;
    }
}
