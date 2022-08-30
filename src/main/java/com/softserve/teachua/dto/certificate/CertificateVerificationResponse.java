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

    private Integer certificateType;

    private String userName;

    private String courseDescription;

    private String projectDescription;

    private String picturePath;

}
