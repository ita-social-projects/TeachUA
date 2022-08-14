package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.User;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@With
public class CertificateTransfer implements Convertible {

    private Long id;
    private Long serialNumber;
    private User user;
    private String userName;
    private String sendToEmail;
    private Boolean sendStatus;
    private LocalDate updateStatus;
    private CertificateTemplate template;
    private CertificateDates dates;

}
