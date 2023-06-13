package com.softserve.club.dto.complaint;

import com.softserve.club.model.Club;
import com.softserve.commons.util.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComplaintResponse implements Convertible {
    private Long id;
    private String text;
    private LocalDate date;
    private Club club;
}
