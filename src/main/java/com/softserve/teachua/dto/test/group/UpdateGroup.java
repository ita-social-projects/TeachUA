package com.softserve.teachua.dto.test.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGroup {
    @NotBlank(message = "Назва групи не може бути порожньою.")
    @CheckRussian(message = "Назва групи містить недопустимі символи.")
    @Size(min = 3, message = "Назва групи повинна містити більше ніж 3 символи." )
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank
    @Size(min = 4, message = "Реєстраційний ключ повинен містити більше ніж 4 символи." )
    private String enrollmentKey;
}
