package com.softserve.teachua.dto.certificate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.certificateExcel.CertificateExcel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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

//    @NotNull
    LocalDate startDate;

//    @NotNull
    LocalDate endDate;

    @NotBlank
    @JsonDeserialize
    String courseNumber;

    @NotBlank
    String studyType;

    @NotNull
    List<CertificateExcel> excelList;
}
