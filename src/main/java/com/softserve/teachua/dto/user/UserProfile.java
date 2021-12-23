package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.Name;
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

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9-\\.]+@([a-zA-Z-]+\\.)+[a-zA-Z-]{2,4}$", message = "is not valid")
    private String email;

    @Name
    private String firstName;

    @Name
    private String lastName;

    @NotBlank
    @Pattern(regexp = "0[0-9]{9}$",message = "Phone can have only numbers and length 10 and starts with 0 ")
    private String phone;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).*$",
            message = "must contain at least one uppercase and lowercase letter")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[~`!@#$%^&()_=+{}\\[\\]/|:;,\"<>?]).*$",
            message = "must contain at least one number and special symbol")
    @Pattern(regexp = "^[^А-Яа-яЇїІіЄєҐґЁёЪъЫыЭэ]+$", message = "must contain only latin letters")
    private String password;

    @NotBlank
    private String roleName;

    private String verificationCode;

    private String urlLogo;

    private String status;
}
