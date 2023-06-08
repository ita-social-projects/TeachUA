package com.softserve.teachua.dto.complaint;

import com.softserve.commons.util.marker.Convertible;
import com.softserve.teachua.model.Club;
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
    //todo
    //private User user;

    private Club club;
}
