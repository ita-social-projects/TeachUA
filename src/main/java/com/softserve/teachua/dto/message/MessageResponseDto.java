package com.softserve.teachua.dto.message;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Getter
@Setter
public class MessageResponseDto implements Convertible {
    private Long id;

    private String text;

    private LocalDateTime date;

    private Long clubId;

    private Long senderId;

    private Long recipientId;

    private Boolean isActive;
}
