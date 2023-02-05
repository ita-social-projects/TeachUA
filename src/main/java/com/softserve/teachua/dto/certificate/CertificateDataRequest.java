package com.softserve.teachua.dto.certificate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.certificate_excel.CertificateExcel;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
