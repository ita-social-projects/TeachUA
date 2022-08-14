package com.softserve.teachua.dto.certificate;


import com.softserve.teachua.dto.certificateDates.CertificateDatesResponse;
import com.softserve.teachua.dto.certificateTemplate.CertificateTemplateResponse;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserProfile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CertificateProfile implements Convertible {
    private Long id;
    @NotBlank
    @Size(min = 10, max = 10, message = "Неправильна довжина серійного номеру.")
    @Pattern(regexp = "^[1-3][0-9]{9}$", message = "Неправильний формат серійного номеру.")
    private Long serialNumber;

    @NotBlank
    private String userName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9-\\.]+@([a-zA-Z-]+\\.)+[a-zA-Z-]{2,4}$", message = "is not valid")
    private String sendToEmail;

    private UserProfile profile;

    private Boolean sendStatus;

    private LocalDate updateStatus;

    @NotBlank
    @Pattern(regexp = "^[123]$")
    private Integer certificateType;

    @Valid
    private CertificateDatesResponse dates;

    @Valid
    private CertificateTemplateResponse template;
}
