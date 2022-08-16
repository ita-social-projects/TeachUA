package com.softserve.teachua.dto.test.question;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.test.validation.annotation.WithoutBlank;
import com.softserve.teachua.utils.test.validation.annotation.MaxAmount;
import com.softserve.teachua.utils.test.validation.annotation.MinAmount;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionProfile {
    private Long id;
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank(message = "Назва групи не може бути порожньою.")
    @CheckRussian(message = "Назва групи містить недопустимі символи.")
    @Size(min = 3, message = "Назва групи повинна містити більше ніж 3 символи." )
    private String title;
    @JsonDeserialize(using = TrimDeserialize.class)
    @CheckRussian(message = "Опис тесту містить недопустимі символи.")
    private String description;
    @NotEmpty(message = "Запитання повинно містити варіанти відповідей.")
    @WithoutBlank(message = "Варіант відповіді не може бути пустим.")
    @MinAmount(min = 2, message = "Максимальна кількість варіантів відповідей - 2.")
    @MaxAmount(max = 5, message = "Мінімальна кількість варіантів відповідей - 5.")
    private List<String> answerTitles;
    @NotBlank(message = "Назва групи не може бути порожньою.")
    private String categoryTitle;
    @Min(value = 1, message = "Відповідь на запитання повинна оцінюватись щонайменше в 1 бал.")
    private Integer value;
    @NotEmpty(message = "Запитання повинно містити щонайменше одну правильну відповідь.")
    private List<Integer> correctAnswerIndexes;
}
