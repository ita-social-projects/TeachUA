package com.softserve.teachua.model.archivable;

import com.softserve.commons.util.marker.Convertible;
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
public class FeedbackArch implements Convertible/*, Archivable*/ {
    private Long id;
    private Float rate;
    private LocalDateTime date;
    private String text;
    private Long userId;
    private Long clubId;

    //todo
    /*
    @Override
    public Class<FeedbackServiceImpl> getServiceClass() {
        return FeedbackServiceImpl.class;
    }
    */
}
