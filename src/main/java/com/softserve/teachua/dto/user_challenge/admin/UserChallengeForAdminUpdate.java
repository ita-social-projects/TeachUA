package com.softserve.teachua.dto.user_challenge.admin;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeForAdminUpdate implements Convertible {
    private Long userChallengeId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String statusName;
}
