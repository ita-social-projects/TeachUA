package com.softserve.certificate.dto.certificate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.certificate.dto.certificate_excel.CertificateExcel;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CertificateDataRequest {
    @NotNull
    Integer type;

    @NotNull
    @Min(value = 1)
    Integer hours;

    LocalDate startDate;

    LocalDate endDate;

    @NotBlank
    @JsonDeserialize
    String courseNumber;

    @NotBlank
    String studyType;

    @NotNull
    List<CertificateExcel> excelList;
}
