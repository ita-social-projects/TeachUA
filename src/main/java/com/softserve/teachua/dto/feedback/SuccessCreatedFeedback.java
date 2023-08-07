package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedFeedback implements Convertible {
    private Long id;

    private Float rate;

    private String text;

    private UserPreview user;

    private Long clubId;

    private List<ReplyResponse> replies;
}
