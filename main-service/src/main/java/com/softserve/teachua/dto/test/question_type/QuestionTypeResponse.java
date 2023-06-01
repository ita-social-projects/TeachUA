package com.softserve.teachua.dto.test.question_type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionTypeResponse {
    private Long id;

    @NotBlank(message = "Заголовок типу питання не може бути пустим.")
    private String title;
}
