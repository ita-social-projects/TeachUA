package com.softserve.club.dto.message;

import com.softserve.club.dto.club.MessagesClub;
import com.softserve.commons.dto.UserPreview;
import com.softserve.commons.util.marker.Convertible;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

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
