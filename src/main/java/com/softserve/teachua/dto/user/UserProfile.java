package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.club.validation.Phone;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserProfile implements Convertible {

    private Long id;

    @NotEmpty
    private String email;


    @NotEmpty
    @Pattern(regexp ="^[^-ЁёЪъЫыЭэ]*$",message = "Last name cannot contain russian letters ")
    private String firstName;


    @NotEmpty
    @Pattern(regexp ="^[^-ЁёЪъЫыЭэё]*$",message = "Last name cannot contain russian letters ")

    private String lastName;

    @NotEmpty
    @Pattern(regexp = "^[0-9]{10}$",message = "Phone can have only numbers and length 10")
    //@Phone
    private String phone;


//    @NotEmpty
    private String password;

    @NotEmpty
    private String roleName;

    private String verificationCode;

    private String urlLogo;

    private String status;

}
