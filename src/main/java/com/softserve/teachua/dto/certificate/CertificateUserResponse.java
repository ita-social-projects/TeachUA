package com.softserve.teachua.dto.certificate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CertificateUserResponse {

    private Long serialNumber;

    private String certificateType;

    private String date;

    private String courseDescription;

}
