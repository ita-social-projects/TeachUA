package com.softserve.club.dto.feedback;

import com.softserve.club.model.Club;
import com.softserve.commons.util.marker.Convertible;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackResponse implements Convertible {
    private Long id;

    private Float rate;

    private String text;

    private LocalDateTime date;
    //todo
    //private User user;

    private Club club;
}
