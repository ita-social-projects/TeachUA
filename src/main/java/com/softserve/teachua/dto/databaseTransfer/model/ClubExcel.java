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

    private String name;
    private List<String> categories;
    private Integer ageFrom;
    private Integer ageTo;
    private String description;

    private String site;
    private String phone;
    private Long clubExternalId;
    private Long centerExternalId;
}
