package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.certificateDates.CertificateDatesResponse;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import java.time.LocalDate;

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
