package com.softserve.teachua.dto.news;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.UserPreview;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NewsProfile implements Convertible {

    @NotBlank(message = "title cannot be empty")
    @CheckRussian
    @Size(min = 10, max = 1500, message = "title should be between 10 and 1500 chars")
    private String title;

    @NotBlank(message = "description cannot be empty")
    @CheckRussian
    @Size(min = 40, max = 15000, message = "description should be between 40 and 15000 chars")
    private String description;

    @NotBlank
    @Size(max = 130, message = "must contain a maximum of 130 letters")
    private String urlTitleLogo;

    @NotNull
    @Pattern(regexp = "^true$|^false$", message = "allowed input: true or false")
    private Boolean isActive;

}
