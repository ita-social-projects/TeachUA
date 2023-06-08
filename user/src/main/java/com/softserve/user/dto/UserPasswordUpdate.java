package com.softserve.user.dto;

import com.softserve.commons.util.marker.Convertible;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserPasswordUpdate implements Convertible {
    private String oldPassword;
    @Size(min = 8, max = 20, message = "The password cannot be shorter than 8 and longer than 20 characters ")
    @Pattern(regexp = "(?m)^(?=[a-zA-Z0-9~`!@#$%^&()_=+{}\\\\|:;\"<>?])(?=.*[a-zA-Z])(?=.*\\d)"
            + "(?=.*[~`!@#$%^&()_=+{}\\\\|:;\"<>?])[a-zA-Z0-9~`!@#$%^&()_=+{}\\\\|:;\"<>?]+$",
            message = "The password must contain uppercase / lowercase letters of the Latin alphabet, "
                    + "numbers and special characters")
    private String newPassword;
    private String newPasswordVerify;
}
