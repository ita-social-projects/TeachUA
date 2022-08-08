package com.softserve.teachua.dto.certificateExcel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CertificateExcel {

    private String name;
    private LocalDate dateIssued;
    private String email;

}
