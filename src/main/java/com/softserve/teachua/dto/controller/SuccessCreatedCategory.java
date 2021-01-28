package com.softserve.teachua.dto.controller;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class SuccessCreatedCategory implements Dto {
    private String name;
}
