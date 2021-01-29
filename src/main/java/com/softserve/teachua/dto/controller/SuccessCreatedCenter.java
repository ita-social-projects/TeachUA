package com.softserve.teachua.dto.controller;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@AllArgsConstructor
@Builder
@Data
public class SuccessCreatedCenter implements Dto {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phones;
    private String description;
    private String urlWeb;
    private String urlLogo;
    private String socialLinks;
    private Double latitude;
    private Double longitude;
    private User user;
}
