package com.softserve.teachua.dto.test.question;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.validations.CheckRussian;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class QuestionProfile {
    private Long id;

    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank(message = "Назва запитання не може бути порожньою.")
    @CheckRussian(message = "Назва запитання містить недопустимі символи.")
    @Size(min = 3, message = "Назва запитання повинна містити більше ніж 3 символи.")
    private String title;

    @JsonDeserialize(using = TrimDeserialize.class)
    @CheckRussian(message = "Опис тесту містить недопустимі символи.")
    private String description;

    @NotEmpty(message = "Запитання повинно містити варіанти відповідей.")
    private List<@NotBlank(message = "Варіант відповіді не може бути порожнім.") String> answerTitles;

    @NotBlank(message = "Назва групи не може бути порожньою.")
    private String categoryTitle;

    @Min(value = 1, message = "Запитання повинно оцінюватись щонайменше в 1 бал.")
    private Integer value;

    @NotEmpty(message = "Запитання повинно містити щонайменше одну правильну відповідь.")
    @Size(min = 1, message = "Запитання повинно містити щонайменше одну правильну відповідь.")
    private List<Integer> correctAnswerIndexes;
}
