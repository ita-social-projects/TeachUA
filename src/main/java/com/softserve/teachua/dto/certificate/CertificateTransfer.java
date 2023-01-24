package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.Messenger;
import com.softserve.teachua.model.User;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

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
    private User user;
    private String userName;
    private String sendToEmail;
    private String messengerUserName;
    private Messenger messenger;
    private Boolean sendStatus;
    private LocalDate updateStatus;
    private CertificateTemplate template;
    private CertificateDates dates;
    private String values;
}
