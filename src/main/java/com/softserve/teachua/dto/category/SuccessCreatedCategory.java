package com.softserve.teachua.dto.category;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class SuccessCreatedCategory implements Convertible {

    private String name;

}

