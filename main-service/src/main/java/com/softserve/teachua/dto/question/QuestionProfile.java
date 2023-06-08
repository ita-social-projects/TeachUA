package com.softserve.teachua.dto.question;

import com.softserve.commons.util.marker.Convertible;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionProfile implements Convertible {
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String text;
}
