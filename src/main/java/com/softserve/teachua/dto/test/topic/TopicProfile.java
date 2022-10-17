package com.softserve.teachua.dto.test.topic;

import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TopicProfile extends RepresentationModel<TopicProfile> {
    @NotBlank(message = "Назва теми тесту не може бути порожньою.")
    @CheckRussian(message = "Назва теми тесту містить недопустимі символи.")
    @Size(min = 3, message = "Назва теми тесту повинна містити більше ніж 3 символи." )
    private String title;
}
