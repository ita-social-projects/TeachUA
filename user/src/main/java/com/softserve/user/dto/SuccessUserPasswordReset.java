package com.softserve.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessUserPasswordReset {
    private Long id;
    private String email;
    private String password;
    private String verificationCode;
}
