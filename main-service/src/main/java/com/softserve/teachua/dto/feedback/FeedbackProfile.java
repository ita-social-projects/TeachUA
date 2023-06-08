package com.softserve.teachua.dto.feedback;

import com.softserve.commons.util.marker.Convertible;
import com.softserve.commons.util.validation.CheckRussian;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackProfile implements Convertible {
    private Long id;

    @NotNull(message = "cannot be null")
    @Min(value = 0, message = "Rate cannot be  less than 0")
    @Max(value = 5, message = "Rate cannot be more than 5 ")
    private Float rate;

    @CheckRussian
    @Size(min = 10, max = 1500, message = " should be between 10 and 1500 symbols")
    private String text;

    @NotNull(message = "cannot be null")
    private Long userId;

    @NotNull(message = "cannot be null")
    private Long clubId;
}
