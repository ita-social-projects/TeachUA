package com.softserve.teachua.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessCreatedTask implements Convertible {
    private Long id;
    private String name;
    private String description;
    private String picture;
    private LocalDate startDate;
    private Long challengeId;
}
