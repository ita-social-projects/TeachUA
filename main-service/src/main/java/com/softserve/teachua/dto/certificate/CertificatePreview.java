package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.certificate_dates.CertificateDatesResponse;
import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import jakarta.validation.constraints.Email;
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
