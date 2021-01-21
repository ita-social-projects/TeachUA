package com.softserve.teachua.dto.controller;

import com.softserve.teachua.model.Club;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class CityResponse {

    private Long id;
    private String name;
    private List<Club> clubs;

}
