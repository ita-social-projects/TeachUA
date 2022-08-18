package com.softserve.teachua.dto.certificateDates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertificateDatesResponse implements Convertible {
    private Long id;
    private Integer hours;
    private String duration;
    private String issuanceDate;
    private String courseNumber;
}
