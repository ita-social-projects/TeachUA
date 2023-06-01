package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.TaskServiceImpl;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskArch implements Convertible, Archivable {
    private String name;
    private String headerText;
    private String description;
    private String picture;
    private Long challengeId;
    private LocalDate startDate;

    @Override
    public Class<TaskServiceImpl> getServiceClass() {
        return TaskServiceImpl.class;
    }
}
