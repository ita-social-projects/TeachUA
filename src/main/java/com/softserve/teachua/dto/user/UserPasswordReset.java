package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class UserPasswordReset implements Convertible {

    //@NotEmpty
    private String email;
    //@NotEmpty
    private String password;

    private String accessToken;

}
