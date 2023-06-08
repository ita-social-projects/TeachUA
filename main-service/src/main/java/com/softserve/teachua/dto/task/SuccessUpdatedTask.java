package com.softserve.teachua.dto.task;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softserve.commons.util.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@With
@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessUpdatedTask implements Convertible {
    private Long id;
    private String name;
    private String headerText;
    private String description;
    private String picture;
    private LocalDate startDate;
    private Long challengeId;
}
