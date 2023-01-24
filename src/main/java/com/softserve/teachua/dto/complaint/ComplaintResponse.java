package com.softserve.teachua.dto.complaint;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
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

    private User user;

    private Club club;
}
