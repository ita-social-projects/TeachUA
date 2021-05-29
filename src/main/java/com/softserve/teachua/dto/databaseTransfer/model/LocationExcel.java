package com.softserve.teachua.dto.databaseTransfer.model;

import lombok.*;

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
