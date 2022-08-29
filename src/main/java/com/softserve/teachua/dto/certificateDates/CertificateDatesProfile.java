package com.softserve.teachua.dto.certificateDates;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class CertificateDatesProfile implements Convertible {
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\d{2}.\\d{2}.\\d{4}", message = "Неправильний формат дати видачі сертифікату.")
    private String date;

    @NotBlank
    private Integer hours;

    @NotBlank
    private String duration;

    @NotBlank
    @Pattern(regexp = "^[0-9]{2}$", message = "Неправильний формат.")
    private String courseNumber;

    private String courseDescription;

    private String projectDescription;

    private String picturePath;

}
