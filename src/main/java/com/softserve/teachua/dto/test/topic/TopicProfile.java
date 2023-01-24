package com.softserve.teachua.dto.test.topic;

import com.softserve.teachua.utils.validations.CheckRussian;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicProfile extends RepresentationModel<TopicProfile> {
    @NotBlank(message = "Назва теми тесту не може бути порожньою.")
    @CheckRussian(message = "Назва теми тесту містить недопустимі символи.")
    @Size(min = 3, message = "Назва теми тесту повинна містити більше ніж 3 символи.")
    private String title;
}
