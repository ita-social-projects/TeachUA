package com.softserve.teachua.dto.contactType;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ContactTypeProfile implements Convertible {

    private Long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String urlLogo;
}
