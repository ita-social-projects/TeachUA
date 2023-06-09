package com.softserve.club.dto.database_transfer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LocationExcel {
    private Long id;
    private String name;
    private String city;
    private String address;
    private String district;
    private String station;

    private Double longitude;
    private Double latitude;

    private Long centerExternalId;
    private Long clubExternalId;
}
