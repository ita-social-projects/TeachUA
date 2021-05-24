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
    private String description;

    private String city;
    private String address;
    private Double longitude;
    private Double latitude;
    private String district;
    private String station;

    // contacts field

    private String phone;
    // site field can include social media too
    private String site;
}
