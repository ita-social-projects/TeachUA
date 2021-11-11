package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.club.validation.Phone;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.Name;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserUpdateProfile implements Convertible {

    private Long id;

    @NotBlank
    private String email;

    @Name
    private String firstName;

    @Name
    private String lastName;

    @NotBlank
    @Pattern(regexp = "[0-9]{10}", message = "Phone number must contain 10 numbers and can`t contain other symbols")
    //@Phone

    private String phone;

    private String urlLogo;

    private String status;

    @NotBlank
    private String roleName;
}
