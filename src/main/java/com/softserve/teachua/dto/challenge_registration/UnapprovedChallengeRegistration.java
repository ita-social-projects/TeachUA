package com.softserve.teachua.dto.challenge_registration;

import com.softserve.teachua.dto.challenge.MessagesChallenge;
import com.softserve.teachua.dto.child.ChildResponse;
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
public class UnapprovedChallengeRegistration implements Convertible {
    private Long id;
    private ParentResponse user;
    private ChildResponse child;
    private MessagesChallenge challenge;
    private String registrationDate;
    private String comment;
}
