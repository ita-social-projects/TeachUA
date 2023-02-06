package com.softserve.teachua.dto.test.question_category;

import com.softserve.teachua.utils.validations.CheckRussian;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class QuestionCategoryProfile extends RepresentationModel<QuestionCategoryProfile> {
    @NotBlank(message = "Назва категорії тесту не може бути порожньою.")
    @CheckRussian(message = "Назва категорії тесту містить недопустимі символи.")
    @Size(min = 3, message = "Назва категорії тесту повинна містити більше ніж 3 символи.")
    private String title;
}
