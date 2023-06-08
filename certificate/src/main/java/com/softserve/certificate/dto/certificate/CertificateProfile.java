package com.softserve.certificate.dto.certificate;

import com.softserve.commons.marker.Convertible;
import com.softserve.certificate.model.CertificateDates;
import com.softserve.certificate.model.CertificateTemplate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String messengerUserName;
    //todo
    //private Messenger messenger;
    //
    //private User user;

    private Boolean sendStatus;

    private LocalDate updateStatus;

    @Valid
    private CertificateDates dates;

    @Valid
    private CertificateTemplate template;
}
