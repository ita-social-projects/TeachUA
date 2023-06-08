package com.softserve.teachua.model.archivable;

import com.softserve.commons.util.marker.Archivable;
import com.softserve.commons.util.marker.Convertible;
import com.softserve.teachua.service.impl.QuestionServiceImpl;
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
public class QuestionArch implements Convertible, Archivable {
    private String title;
    private String text;

    @Override
    public Class<QuestionServiceImpl> getServiceClass() {
        return QuestionServiceImpl.class;
    }
}
