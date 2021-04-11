package com.softserve.teachua.dto.databaseTransfer.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CenterExcel implements Convertible {
    private Long id;
    private String name;
    private String city;
    private String address;
    private Double longitude;
    private Double altitude;
    private String district;
    private String station;
    private String site;
    private String phone;
    private String description;
}
