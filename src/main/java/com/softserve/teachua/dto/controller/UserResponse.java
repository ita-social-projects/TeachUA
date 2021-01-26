package com.softserve.teachua.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String roleName;
}
