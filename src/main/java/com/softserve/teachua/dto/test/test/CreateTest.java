package com.softserve.teachua.dto.test.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.validations.CheckRussian;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CreateTest {
    @JsonDeserialize(using = TrimDeserialize.class)
    @NotBlank(message = "Назва тесту не може бути порожньою.")
    @CheckRussian(message = "Назва тесту містить недопустимі символи.")
    @Size(min = 3, message = "Назва тесту повинна містити більше ніж 3 символи.")
    private String title;
    @JsonDeserialize(using = TrimDeserialize.class)
    @CheckRussian(message = "Опис тесту містить недопустимі символи.")
    private String description;
    @Positive(message = "Значення складності тесту не може бути від'ємним.")
    @NotNull(message = "Вказане поле не може бути порожнім.")
    @Min(value = 1, message = "1 - мінімальне значення складності тесту.")
    @Max(value = 10, message = "10 - Максимальне значення складності тесту.")
    private int difficulty;
    @Positive(message = "Значення тривалості проходження не може бути від'ємним.")
    @NotNull(message = "Вказане поле не може бути порожнім.")
    @Min(value = 1, message = "Тривалість тесту повинна бути рівною щонайменше 1 хвилині.")
    private int duration;
    @NotBlank
    private String topicTitle;
    @ToString.Exclude
    // @NotEmpty(message = "Тест повинен містити запитання.")
    // @Size(min = 3, message = "Мінімальна допустима кількість запитань в тесті - 3.")
    private List<QuestionProfile> questions;

    public void addQuestion(QuestionProfile question) {
        questions.add(question);
    }
}
