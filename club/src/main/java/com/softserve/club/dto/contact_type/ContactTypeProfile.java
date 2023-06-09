package com.softserve.club.dto.contact_type;

import com.softserve.commons.util.marker.Convertible;
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
