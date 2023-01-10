package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CertificateServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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
    private boolean sendStatus;
    private LocalDate updateStatus;
    private Integer templateId;
    private Integer datesId;
    private String values;

    @Override
    public Class getServiceClass() {
        return CertificateServiceImpl.class;
    }
}
