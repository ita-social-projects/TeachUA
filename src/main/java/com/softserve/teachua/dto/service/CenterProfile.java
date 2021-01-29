package com.softserve.teachua.dto.service;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Builder
@Data
public class CenterProfile implements Dto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phones;

    @NotEmpty
    private String description;

    @NotEmpty
    private String urlWeb;

    @NotEmpty
    private String urlLogo;

    @NotEmpty
    private String socialLinks;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private User user;

}
