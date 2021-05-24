package com.softserve.teachua.dto.databaseTransfer.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@With
public class ClubExcel implements Convertible {
    private Long id;
    private String name;
    private Long centerId;
    private List<String> categories;
    private Integer ageFrom;
    private Integer ageTo;
    private String description;

    // fields from location class , todo delete in future
    private String city;
    private String address;
    private Double longitude;
    private Double latitude;
    private String district;
    private String station;

    private String site;
    private String phone;
}
