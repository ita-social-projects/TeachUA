package com.softserve.teachua.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserVerifyPassword {
    @NotNull
    private Long id;

    private String password;
}
