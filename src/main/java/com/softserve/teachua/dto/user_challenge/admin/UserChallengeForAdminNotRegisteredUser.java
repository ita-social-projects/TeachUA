package com.softserve.teachua.dto.user_challenge.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserChallengeForAdminNotRegisteredUser {
    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String roleName;
}
