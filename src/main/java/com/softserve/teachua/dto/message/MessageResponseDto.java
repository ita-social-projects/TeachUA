package com.softserve.teachua.dto.message;

import com.softserve.teachua.dto.club.MessagesClub;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
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

    private MessagesClub club;

    private UserPreview sender;

    private UserPreview recipient;

    private Boolean isActive;
}
