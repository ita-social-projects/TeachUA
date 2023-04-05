package com.softserve.teachua.dto.test.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softserve.teachua.utils.validations.CheckRussian;
import java.time.LocalDate;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class GroupProfile extends RepresentationModel<GroupProfile> {
    private Long id;

    @NotBlank(message = "Назва групи не може бути порожньою.")
    @CheckRussian(message = "Назва групи містить недопустимі символи.")
    @Size(min = 3, message = "Назва групи повинна містити більше ніж 3 символи.")
    private String title;

    @NotBlank(message = "Код входу до групи не може бути порожнім.")
    @CheckRussian(message = "Код входу до групи містить недопустимі символи.")
    @Size(min = 3, message = "Код входу до групи повинен містити більше ніж 3 символи.")
    private String enrollmentKey;

    @FutureOrPresent(message = "Дата повинна бути сьогоднішньою або майбутньою.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Future(message = "Дата повинна бути майбутньою.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
