package com.softserve.teachua.dto.question;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionResponse implements Convertible {
    private Long id;

    private String title;

    private String text;
}
