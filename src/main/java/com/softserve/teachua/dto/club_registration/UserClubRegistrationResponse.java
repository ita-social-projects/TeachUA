package com.softserve.teachua.dto.club_registration;

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
    private String registrationDate;
    private boolean isApproved;
    private boolean isActive;
    private String comment;
}
