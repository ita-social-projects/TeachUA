package com.softserve.teachua.model.archivable;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.marker.Archivable;
import com.softserve.teachua.service.impl.QuestionServiceImpl;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@With
@Builder
public class QuestionArch implements Convertible, Archivable {
    private String title;
    private String text;

    @Override
    public Class getServiceClass() {
        return QuestionServiceImpl.class;
    }
}
