package com.softserve.teachua.dto.controller.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class SuccessDeletedClubRating {
    private Integer clubId;
    private Integer ratingId;
}
