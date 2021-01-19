package com.softserve.teachua.dto.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Data
public class ActivityProfile {
    @NotEmpty
    private String name;

    //For @RequestBody, will delete
    @NotEmpty
    private String secondField;
}