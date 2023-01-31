package com.softserve.teachua.dto.news;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import java.time.LocalDate;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsProfile implements Convertible {
    @NotNull
    @FutureOrPresent(message = "Дата повинна бути сьогоднішньою або майбутньою.")
    private LocalDate date;

    @NotBlank(message = "Заголовок не може бути порожнім.")
    @CheckRussian
    @Size(min = 10, max = 1500, message = "Заголовок повинен бути від 10 до 1500 символів.")
    private String title;

    @NotBlank(message = "Текст новини не може бути порожнім.")
    @CheckRussian
    @Size(min = 40, max = 15000, message = "Текст новини повинен бути від 40 до 15000 символів")
    private String description;

    @NotBlank
    @Size(max = 517, message = "Назва фото баннера повинна містити максимум 500 символів.")
    @Pattern(regexp = "/upload/news/[^/]+\\.[A-z]{3,5}",
            message = "Неправильний шлях до файлу. Має виглядати як /upload/news/*.png")
    private String urlTitleLogo;

    // @NotNull
    // @Pattern(regexp = "^true$|^false$", message = "allowed input: true or false")
    private Boolean isActive;
}
