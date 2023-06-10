package com.softserve.question.dto.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softserve.commons.util.validation.CheckRussian;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateGroup {
    @NotBlank(message = "Назва групи не може бути порожньою.")
    @CheckRussian(message = "Назва групи містить недопустимі символи.")
    @Size(min = 3, message = "Назва групи повинна містити більше ніж 3 символи.")
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank
    @Size(min = 4, message = "Реєстраційний ключ повинен містити більше ніж 4 символи.")
    private String enrollmentKey;
}
