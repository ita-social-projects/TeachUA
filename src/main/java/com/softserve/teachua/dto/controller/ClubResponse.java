package com.softserve.teachua.dto.controller;

import com.softserve.teachua.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@AllArgsConstructor
@Data
@Builder
public class ClubResponse {
    private Long id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String urlWeb;
    private String urlLogo;
    private String workTime;
    private City city;
    private Set<Category> categories;
    private Set <Coordinates> coordinates;
    private User user;
    private Studio studio;
}
