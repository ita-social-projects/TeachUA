package com.softserve.certificate.dto.schedule;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.config.ScheduledTask;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskSchedule {
    private Set<ScheduledTask> tasks;
}
