package com.softserve.teachua.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class SuccessCreatedClub {
    private int ageFrom;
    private int ageTo;
    private String name;
}
