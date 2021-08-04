package com.softserve.teachua.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessUserPasswordReset {
    public Long id;
    public String email;
    public String password;
    public String verificationCode;
}
