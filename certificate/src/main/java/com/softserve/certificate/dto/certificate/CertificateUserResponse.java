package com.softserve.certificate.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CertificateUserResponse {
    private Long id;
    private Long serialNumber;
    private String certificateTypeName;
    private String date;
    private String courseDescription;
}
