package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.CertificateServiceImpl;
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
    private Long datesId;
    private String values;

    @Override
    public Class<CertificateServiceImpl> getServiceClass() {
        return CertificateServiceImpl.class;
    }
}
