package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.DurationEntityServiceImpl;
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
public class DurationEntityArch implements Convertible, Archivable {
    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    @Override
    public Class getServiceClass() {
        return DurationEntityServiceImpl.class;
    }
}
