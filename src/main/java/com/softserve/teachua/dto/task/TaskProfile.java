package com.softserve.teachua.dto.task;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskProfile implements Convertible {
    private Long id;
    private String name;
    private String headerText;
    private String description;
    private String picture;
    private LocalDate startDate;
    private Long challengeId;
    private Boolean isActive;
}
