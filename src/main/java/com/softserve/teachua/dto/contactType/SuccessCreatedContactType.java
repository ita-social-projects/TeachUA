package com.softserve.teachua.dto.contactType;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedContactType implements Convertible {
    private Long id;

    private String name;

    private String urlLogo;
}
