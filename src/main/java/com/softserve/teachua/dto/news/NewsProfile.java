package com.softserve.teachua.dto.news;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsProfile implements Dto {

    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;
}
