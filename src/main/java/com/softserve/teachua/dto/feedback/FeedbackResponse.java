package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.club.MessagesClub;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserResponse;
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

    private String date;

    private UserResponse user;

    private MessagesClub club;
}
