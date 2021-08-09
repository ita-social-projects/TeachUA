package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserUpdateProfile implements Convertible {

    private Long id;

    private String email;

    private String firstName;

    private String lastName;

    private String phone;

    private String password;

    private String urlLogo;

    private String status;

    private String roleName;
}
