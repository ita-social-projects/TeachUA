package com.softserve.teachua.dto.test.question;

import com.softserve.teachua.dto.test.answer.AnswerResponse;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private Long id;

    @NotBlank(message = "Заголовок питання не може бути пустим.")
    private String title;

    @NotBlank(message = "Опис питання не може бути пустим.")
    private String description;

    @NotBlank(message = "Тип питання не може бути пустим.")
    private String questionTypeTitle;

    @NotBlank(message = "Категорія питання не може бути пустою.")
    private String questionCategoryTitle;
    private List<String> answerTitles;

    @NotNull
    @Valid
    private List<AnswerResponse> answers;
}
