package com.softserve.teachua.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Data

public class UserProfile {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
