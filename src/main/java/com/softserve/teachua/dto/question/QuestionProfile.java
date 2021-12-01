package com.softserve.teachua.dto.question;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

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
