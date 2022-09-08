package com.softserve.teachua.dto.certificateExcel;

import com.softserve.teachua.utils.validations.CertificateUserName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CertificateExcel {

    @CertificateUserName
    private String name;
    @DateTimeFormat(pattern = "d.MM.yyyy")
    @FutureOrPresent(message = "Дата видачі сертифікату не може бути в минулому")
    private LocalDate dateIssued;
    @Email(message = "Неможливо розпізнати електронну адресу")
    private String email;

}
