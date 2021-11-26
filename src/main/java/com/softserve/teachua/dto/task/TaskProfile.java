package com.softserve.teachua.dto.task;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskProfile implements Convertible, Archivable {
    private Long id;
    private String name;
    private String headerText;
    private String description;
    private String picture;
    private LocalDate startDate;
    private Long challengeId;
}
