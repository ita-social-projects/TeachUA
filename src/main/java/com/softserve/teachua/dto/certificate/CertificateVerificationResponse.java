package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Getter
@Setter
public class CertificateVerificationResponse implements Convertible {

    private Long serialNumber;

    private String date;

    private String duration;

    private String userName;

    private String description;
}
