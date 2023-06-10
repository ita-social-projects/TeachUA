package com.softserve.question.dto.question_category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCategoryResponse {
    private Long id;

    @NotBlank(message = "Заголовок категоріі питання не може бути пустим.")
    private String title;
}
