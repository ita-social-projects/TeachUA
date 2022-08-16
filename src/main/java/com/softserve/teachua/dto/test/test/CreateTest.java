package com.softserve.teachua.dto.test.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.softserve.teachua.dto.test.question.QuestionProfile;
import com.softserve.teachua.utils.deserializers.TrimDeserialize;
import com.softserve.teachua.utils.test.validation.annotation.MaxAmount;
import com.softserve.teachua.utils.test.validation.annotation.MinAmount;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.List;

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
    @NotNull(message = "Вказане поле не може бути порожнім.")
    @Min(value = 1, message = "1 - мінімальне значення складності тесту.")
    @Max(value = 10, message = "10 - Максимальне значення складності тесту.")
    private int difficulty;
    @NotNull(message = "Вказане поле не може бути порожнім.")
    @Min(value = 1, message = "Тривалість тесту повинна бути рівною щонайменше 1 хвилині.")
    private int duration;
    @NotBlank
    private String topicTitle;
    @ToString.Exclude
    @NotEmpty(message = "Тест повинен містити запитання.")
    @MinAmount(min = 3, message = "Мінімальна кількість запитань - 3.")
    @MaxAmount(max = 10, message = "Максимальна кількість запитань - 10.")
    private List<QuestionProfile> questions;
}
