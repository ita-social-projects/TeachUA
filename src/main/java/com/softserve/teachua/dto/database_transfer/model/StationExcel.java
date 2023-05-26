package com.softserve.teachua.dto.database_transfer.model;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StationExcel implements Convertible {
    private String name;
    private String city;
}
