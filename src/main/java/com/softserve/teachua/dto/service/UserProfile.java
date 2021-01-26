package com.softserve.teachua.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Data
public class UserProfile {
    @NotEmpty
    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;
}
