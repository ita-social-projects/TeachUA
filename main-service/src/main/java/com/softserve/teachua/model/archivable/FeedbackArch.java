package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.FeedbackServiceImpl;
import java.time.LocalDateTime;
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
public class FeedbackArch implements Convertible, Archivable {
    private Long id;
    private Float rate;
    private LocalDateTime date;
    private String text;
    private Long userId;
    private Long clubId;

    @Override
    public Class<FeedbackServiceImpl> getServiceClass() {
        return FeedbackServiceImpl.class;
    }
}
