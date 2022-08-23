package com.softserve.teachua.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.config.ScheduledTask;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskSchedule {
    private Set<ScheduledTask> tasks;
}
