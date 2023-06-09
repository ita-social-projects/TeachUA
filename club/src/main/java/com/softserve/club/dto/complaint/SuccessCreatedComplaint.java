package com.softserve.club.dto.complaint;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedComplaint implements Convertible {
    private Long id;

    private String text;

    private Long userId;

    private Long clubId;
}
