package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.TaskServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    public Class getServiceClass() {
        return TaskServiceImpl.class;
    }
}
