package com.softserve.teachua.dto.test.topic;

import com.softserve.commons.util.validation.CheckRussian;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TopicProfile extends RepresentationModel<TopicProfile> {
    @NotBlank(message = "Назва теми тесту не може бути порожньою.")
    @CheckRussian(message = "Назва теми тесту містить недопустимі символи.")
    @Size(min = 3, message = "Назва теми тесту повинна містити більше ніж 3 символи.")
    private String title;
}
