package com.softserve.teachua.dto.task;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class SuccessCreatedTask implements Convertible {
    private Long id;
    private String name;
    private String headerText;
    private String description;
    private String picture;
    private LocalDate startDate;
    private Long challengeId;
}
