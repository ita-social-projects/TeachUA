package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.Name;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank
    private String email;

    @Name
    private String firstName;

    @Name
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{10,12}$",
            message = "Phone number must contain 10 numbers and can`t contain other symbols")
    private String phone;

    private String urlLogo;

    private String status;

    @NotBlank
    private String roleName;
}
