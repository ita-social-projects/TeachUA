package com.softserve.teachua.dto.club_registration;

import java.time.LocalDateTime;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserClubRegistrationResponse implements Convertible {
    private Long id;
    private Long userId;
    private Long clubId;
    private LocalDateTime registrationDate;
    private boolean isApproved;
    private boolean isActive;
    private String comment;
}
