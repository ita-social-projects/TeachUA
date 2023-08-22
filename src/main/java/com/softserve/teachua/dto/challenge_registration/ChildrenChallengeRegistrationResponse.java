package com.softserve.teachua.dto.challenge_registration;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChildrenChallengeRegistrationResponse implements Convertible {
    private Long id;
    private Long childId;
    private Long challengeId;
    private String registrationDate;
    private boolean isApproved;
    private boolean isActive;
    private String comment;
}
