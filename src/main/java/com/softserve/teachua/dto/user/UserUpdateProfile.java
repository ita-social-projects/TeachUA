package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.club.validation.Phone;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserUpdateProfile implements Convertible {

    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[a-zA-Zа-яА-ЯіІєЄїЇґҐ]*$", message = "First name can contain only ukrainian and english letters")
    @Pattern(regexp = "^[^ЁёЪъЫыЭэ]*$", message = "First name cannot contain russian letters ")
    private String firstName;

    @NotBlank
    @Size(min = 1, max = 25)
    @Pattern(regexp = "[a-zA-Zа-яА-ЯіІєЄїЇґҐ]*$", message = "Last name can contain only ukrainian and english letters")
    @Pattern(regexp = "^[^ЁёЪъЫыЭэ]*$", message = "Last name cannot contain russian letters ")
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
