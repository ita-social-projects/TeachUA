package com.softserve.teachua.dto.controller;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResponse implements Dto {
    private Long id;
    private String name;
    private String urlLogo;
    private String backgroundColor;
    private Set<Club> clubs;
}
