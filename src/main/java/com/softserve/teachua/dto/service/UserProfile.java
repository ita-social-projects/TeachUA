package com.softserve.teachua.dto.service;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserProfile {
    
    @NotEmpty
    private String email;
    
    @NotEmpty
    private String password;
    
    @NotEmpty
    private String name;
    
}
