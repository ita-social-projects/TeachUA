package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.Club;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackResponse implements Dto {

    private Long id;

    private String userName;

    private Float rate;

    private String text;

    private Club club;

}
