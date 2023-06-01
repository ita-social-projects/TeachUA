package com.softserve.teachua.dto.certificate_excel;

import com.softserve.teachua.utils.validations.CertificateUserName;
import java.time.LocalDate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

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
    @NotBlank
    @Email(message = "Неможливо розпізнати електронну адресу")
    private String email;
}
