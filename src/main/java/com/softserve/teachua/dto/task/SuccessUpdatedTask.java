package com.softserve.teachua.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessUpdatedTask implements Convertible {
    private Long id;
    private String name;
    private String description;
    private String picture;
    private LocalDate startDate;
    private Long challengeId;
}
