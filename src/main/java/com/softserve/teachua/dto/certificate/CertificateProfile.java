package com.softserve.teachua.dto.certificate;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.CertificateDates;
import com.softserve.teachua.model.CertificateTemplate;
import com.softserve.teachua.model.Messenger;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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

    private String messengerUserName;

    private Messenger messenger;

    private User user;

    private Boolean sendStatus;

    private LocalDate updateStatus;

    @Valid
    private CertificateDates dates;

    @Valid
    private CertificateTemplate template;
}
