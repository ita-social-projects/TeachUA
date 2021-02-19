package com.softserve.teachua.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserLogin {
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
