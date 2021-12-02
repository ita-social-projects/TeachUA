package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Club;
import com.softserve.teachua.model.User;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.FeedbackServiceImpl;
import lombok.*;

import java.time.LocalDateTime;

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
    public Class getServiceClass() {
        return FeedbackServiceImpl.class;
    }
}