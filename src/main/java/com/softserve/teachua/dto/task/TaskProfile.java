package com.softserve.teachua.dto.task;

import com.softserve.teachua.dto.marker.Convertible;

import java.time.LocalDate;

public class TaskProfile implements Convertible {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
}
