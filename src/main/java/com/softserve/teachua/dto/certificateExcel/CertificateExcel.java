package com.softserve.teachua.dto.certificateExcel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CertificateExcel {

    private String name;
    @DateTimeFormat(pattern = "dd.MM.YYYY")
    private LocalDate dateIssued;
    @Email
    private String email;

}
