package com.softserve.teachua.model.archivable;

import com.softserve.commons.util.marker.Convertible;
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
public class ComplaintArch implements Convertible/*, Archivable*/ {
    private Long id;
    private LocalDate date;
    private Long userId;
    private Long clubId;
    private String text;

    //todo@
    /*
    @Override
    public Class<ComplaintServiceImpl> getServiceClass() {
        return ComplaintServiceImpl.class;
    }
    */
}
