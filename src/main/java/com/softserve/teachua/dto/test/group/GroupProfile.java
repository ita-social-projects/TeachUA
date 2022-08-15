package com.softserve.teachua.dto.test.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class GroupProfile extends RepresentationModel<GroupProfile> {
    @NotBlank(message = "Назва групи не може бути порожньою.")
    @CheckRussian(message = "Назва групи містить недопустимі символи.")
    @Size(min = 3, message = "Назва групи повинна містити більше ніж 3 символи." )
    private String title;

    @NotBlank(message = "Код входу до групи не може бути порожнім.")
    @CheckRussian(message = "Код входу до групи містить недопустимі символи.")
    @Size(min = 3, message = "Код входу до групи повинен містити більше ніж 3 символи." )
    private String enrollmentKey;

    @FutureOrPresent(message="Дата повинна бути сьогоднішньою або майбутньою.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Future(message="Дата повинна бути майбутньою.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
