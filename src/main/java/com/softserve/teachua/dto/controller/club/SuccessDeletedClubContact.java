package com.softserve.teachua.dto.controller.club;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class SuccessDeletedClubContact {
    private Integer clubId;
    private Integer contactId;
}
