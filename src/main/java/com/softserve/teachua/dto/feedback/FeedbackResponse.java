package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackResponse implements Convertible {
    private Long id;

    private Float rate;

    private String text;

    private LocalDateTime date;

    private User user;

    private Club club;
}
