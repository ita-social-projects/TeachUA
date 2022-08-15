package com.softserve.teachua.dto.test.question;

import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.Data;
import org.hibernate.annotations.Check;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionProfile {
    private Long id;

    @NotBlank(message = "Назва запитання не може бути порожньою.")
    @CheckRussian(message = "Назва запитання містить недопустимі символи.")
    @Size(min = 3, message = "Назва запитання повинна містити більше ніж 3 символи.")
    private String title;

    @NotBlank(message = "Текст запитання не може бути порожнім.")
    @CheckRussian(message = "Текст запитання містить недопустимі символи.")
    @Size(min = 3, message = "Текст запитання повинен містити більше ніж 3 символи.")
    private String description;

    @Size(min = 2, message = "Варіантів відповіді повинно бути більше ніж 1.")
    private List<@NotBlank(message = "Варіант відповіді не може бути порожнім.") String> answerTitles;

    @NotBlank(message = "Назва категорії не може бути порожньою.")
    @CheckRussian(message = "Назва категорії містить недопустимі символи.")
    @Size(min = 3, message = "Назва категорії повинна містити більше ніж 3 символи.")
    private String categoryTitle;

    @Min(1)
    private Integer value;

    @Size(min = 1, message = "Правильних варіантів відповіді повинно бути не менше 1.")
    private List<Integer> correctAnswerIndexes;
}
