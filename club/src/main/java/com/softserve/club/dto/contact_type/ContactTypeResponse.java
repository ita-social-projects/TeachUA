package com.softserve.club.dto.contact_type;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ContactTypeResponse implements Convertible {
    private Long id;

    private String name;

    private String urlLogo;
}
