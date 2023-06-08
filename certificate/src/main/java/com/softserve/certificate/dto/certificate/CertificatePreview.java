package com.softserve.certificate.dto.certificate;

import com.softserve.certificate.dto.certificate_dates.CertificateDatesResponse;
import com.softserve.commons.marker.Convertible;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CertificatePreview implements Convertible {
    private Long id;
    private String userName;
    @Email
    private String sendToEmail;
    private Boolean sendStatus;
    private Long serialNumber;
    private LocalDate updateStatus;
    private CertificateDatesResponse dates;
}
