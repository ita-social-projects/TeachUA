package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.ComplaintServiceImpl;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class ComplaintArch implements Convertible, Archivable {
    private Long id;
    private LocalDate date;
    private Long userId;
    private Long clubId;
    private String text;

    @Override
    public Class getServiceClass() {
        return ComplaintServiceImpl.class;
    }
}
