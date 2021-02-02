package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedFeedback implements Dto {
    private Long id;

    private String userName;

    private Float rate;

    private String text;

    private Club club;
}
