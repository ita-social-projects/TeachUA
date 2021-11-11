package com.softserve.teachua.dto.task;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskProfile implements Convertible {
    private Long id;
    private String name;
    private String headerText;
    private String description;
    private String picture;
    private LocalDate startDate;
    private Long challengeId;
}
