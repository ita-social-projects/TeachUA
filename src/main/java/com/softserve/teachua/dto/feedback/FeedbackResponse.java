package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.club.ClubResponse;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import java.util.List;
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
    private UserPreview user;
    private ClubResponse club;
    private List<ReplyResponse> replies;
}
