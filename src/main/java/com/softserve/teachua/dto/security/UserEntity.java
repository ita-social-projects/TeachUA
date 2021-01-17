package com.softserve.teachua.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserEntity {

    private Long id;
    private String email;
    private String password;
    private String roleName;

}
