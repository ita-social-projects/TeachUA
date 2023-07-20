package com.softserve.teachua.dto.club_registration;

import com.softserve.teachua.dto.child.ChildResponse;
import com.softserve.teachua.dto.club.MessagesClub;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.ParentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FullClubRegistration implements Convertible {
    private Long id;
    private ParentResponse user;
    private ChildResponse child;
    private MessagesClub club;
    private String registrationDate;
    private boolean isApproved;
    private boolean isActive;
    private String comment;
}
