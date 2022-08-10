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
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CertificateDataResponse {
    @NotNull
    Integer template;
    @NotNull
    @Min(value = 1)
    Integer hours;
    @NotBlank
    @JsonDeserialize
    String duration;
    @NotBlank
    @JsonDeserialize
    String courseNumber;
    @NotNull
    Integer certificateType;
    @NotNull
    List<CertificateExcel> excelList;
}
