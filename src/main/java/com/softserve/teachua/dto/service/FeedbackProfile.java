package com.softserve.teachua.dto.service;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.Club;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class FeedbackProfile implements Dto {

    @NotEmpty
    private String userName;

    @NotNull
    private Float rate;

    @NotEmpty
    private String text;

    private Club club;
}
