package com.softserve.teachua.dto.controller;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessCreatedNews implements Dto {

    private String title;

    private String description;
}
