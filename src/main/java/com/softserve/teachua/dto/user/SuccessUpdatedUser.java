package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessUpdatedUser implements Convertible {

    private String firstName;
    private String lastName;
    private String phone;
    private Long id;
    private String email;
    private String roleName;
    private String urlLogo;

}
