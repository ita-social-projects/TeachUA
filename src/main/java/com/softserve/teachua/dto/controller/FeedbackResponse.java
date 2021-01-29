package com.softserve.teachua.dto.controller;

import com.softserve.teachua.dto.marker.Dto;
import com.softserve.teachua.model.Club;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackResponse implements Dto {
    private Long id;

    private String userName;

    private Float rate;

    private String text;

    private Club club;
}
