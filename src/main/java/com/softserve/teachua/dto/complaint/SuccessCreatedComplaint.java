package com.softserve.teachua.dto.complaint;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedComplaint implements Convertible {
    private Long id;

    private String text;

    private Long userId;

    private Long clubId;

    private Long recipientId;

    private Boolean isActive;

    private Boolean hasAnswer;
}
