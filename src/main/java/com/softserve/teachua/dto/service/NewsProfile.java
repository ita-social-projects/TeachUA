package com.softserve.teachua.dto.service;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsProfile implements Dto {

    private String title;

    private String description;
}
