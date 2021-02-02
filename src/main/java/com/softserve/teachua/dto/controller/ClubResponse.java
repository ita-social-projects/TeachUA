package com.softserve.teachua.dto.controller;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.Category;
import com.softserve.teachua.model.Center;
import com.softserve.teachua.model.City;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClubResponse implements Dto {
    private Long id;
    private Integer ageFrom;
    private Integer ageTo;
    private String name;
    private String email;
    private String address;
    private String phones;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String workTime;
    private String socialLinks;
    private Double latitude;
    private Double longitude;
    private City city;
    private Set<Category> categories;
    private User user;
    private Center center;
    private Double rating;
}
