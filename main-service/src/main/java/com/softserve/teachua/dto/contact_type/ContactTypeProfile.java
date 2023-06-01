package com.softserve.teachua.dto.contact_type;

import com.softserve.teachua.dto.marker.Convertible;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
