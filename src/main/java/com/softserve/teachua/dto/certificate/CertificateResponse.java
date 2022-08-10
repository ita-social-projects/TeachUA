package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.certificateDates.CertificateDatesResponse;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateResponse;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@With
public class CertificateResponse implements Convertible {

    private Long id;
    private Long serialNumber;
    private String userName;
    private String userEmail;
    private UserPreview user;
    private Integer type;
    private CertificateDatesResponse dates;
    private CertificateTemplateResponse template;
    private Boolean sendStatus;
    private LocalDate updateStatus;

}
