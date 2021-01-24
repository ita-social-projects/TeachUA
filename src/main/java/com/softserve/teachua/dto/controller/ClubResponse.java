package com.softserve.teachua.dto.controller;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ClubResponse {
    private Long id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String urlWeb;
}
