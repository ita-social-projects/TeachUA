package com.softserve.teachua.dto.test.question;

<<<<<<< HEAD
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.test.validation.annotation.WithoutBlank;
import com.softserve.teachua.utils.test.validation.annotation.MaxAmount;
import com.softserve.teachua.utils.test.validation.annotation.MinAmount;
=======
>>>>>>> fedf2288ce4c9f8f8af264a86081bb244ebdefbb
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.Data;
import org.hibernate.annotations.Check;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
<<<<<<< HEAD
import javax.validation.constraints.NotEmpty;
=======
>>>>>>> fedf2288ce4c9f8f8af264a86081bb244ebdefbb
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class QuestionProfile {
    private Long id;
<<<<<<< HEAD
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
=======

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
>>>>>>> fedf2288ce4c9f8f8af264a86081bb244ebdefbb
    private List<Integer> correctAnswerIndexes;
}
