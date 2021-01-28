package com.softserve.teachua.dto.service;


import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Data
public class CenterProfile implements Dto {

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String address;

    @NotNull
    private String phones;

    private String description;

    @NotNull
    private String urlWeb;

    @NotNull
    private String urlLogo;

    private String socialLinks;

    private Double latitude;

    private Double longitude;

    private User user;

}
