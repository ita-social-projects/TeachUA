package com.softserve.teachua.dto.duration_entity;

import com.softserve.teachua.dto.marker.Convertible;
import java.time.LocalDate;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DurationEntityResponse implements Convertible {
    @NotNull(message = "Введіть початкову дату")
    private LocalDate startDate;

    @NotNull(message = "Введіть кінцеву дату")
    private LocalDate endDate;
}
