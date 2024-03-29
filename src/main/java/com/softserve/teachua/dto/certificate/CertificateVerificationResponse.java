package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Getter
@Setter
public class CertificateVerificationResponse implements Convertible {
    private Long serialNumber;

    private String certificateTypeName;

    private String userName;

    private String courseDescription;

    private String projectDescription;

    private String picturePath;
}
