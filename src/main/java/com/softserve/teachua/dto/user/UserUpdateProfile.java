package com.softserve.teachua.dto.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.marker.Convertible;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserUpdateProfile implements Convertible {

    private Long id;

    //@NotEmpty
    private String email;

    //@NotEmpty
    private String firstName;

    //@NotEmpty
    private String lastName;

    //@NotEmpty
    private String phone;

    //@NotEmpty
    private String password;

    //private byte[] urlLogo;
    private String urlLogo;

    private String status;

    private String roleName;
}
