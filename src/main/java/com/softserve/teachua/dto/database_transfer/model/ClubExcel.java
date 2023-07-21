package com.softserve.teachua.dto.database_transfer.model;

import com.softserve.teachua.dto.marker.Convertible;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

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
