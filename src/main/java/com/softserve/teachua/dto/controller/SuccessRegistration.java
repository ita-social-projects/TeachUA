package com.softserve.teachua.dto.controller;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessRegistration implements Dto {

    private Long id;
    private String email;
    private String roleName;

}
