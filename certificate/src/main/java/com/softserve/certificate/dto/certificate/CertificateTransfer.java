package com.softserve.certificate.dto.certificate;

import com.softserve.commons.util.marker.Convertible;
import com.softserve.certificate.model.CertificateDates;
import com.softserve.certificate.model.CertificateTemplate;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@With
@ToString
public class CertificateTransfer implements Convertible {
    private Long id;
    private Long serialNumber;
    //todo
    //private User user;
    private String userName;
    private String sendToEmail;
    private String messengerUserName;
    //todo
    //private Messenger messenger;
    private Boolean sendStatus;
    private LocalDate updateStatus;
    private CertificateTemplate template;
    private CertificateDates dates;
    private String values;
}
