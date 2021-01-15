package com.softserve.teachua.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class SuccessRegistration {

	private Long id;
    private String email;
    private String roleName;
    
}
