package com.softserve.club.dto.feedback;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedFeedback implements Convertible {
    private Long id;

    private Float rate;

    private String text;

    private Long userId;

    private Long clubId;
}
