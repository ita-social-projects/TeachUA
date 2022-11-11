package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.ByteArrayInputStream;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CertificateContent implements Convertible {
    private Long id;

    @NotBlank
    @Size(min = 10, max = 10, message = "Неправильна довжина серійного номеру.")
    @Pattern(regexp = "^[1-3][0-9]{9}", message = "Неправильний формат серійного номеру.")
    private Long serialNumber;

    @NotBlank
    @Pattern(regexp = "^\\d{2}.\\d{2}.\\d{4}", message = "Неправильний формат дати видачі сертифікату.")
    private String issuanceDate;

    @NotBlank
    private String userName;

    @NotBlank
    private String studyHours;

    @NotBlank
    private String studyDuration;

    @NotBlank
    private ByteArrayInputStream qrCode;

    private String templatePath;

    private String studyForm;
}
