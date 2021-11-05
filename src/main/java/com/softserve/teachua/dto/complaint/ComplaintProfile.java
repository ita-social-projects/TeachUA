package com.softserve.teachua.dto.complaint;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ComplaintProfile implements Convertible {
    private Long id;

    @NotEmpty
    @Length(min = 30,max = 1500,message = "should be between 30 and 1500 symbols")
    private String text;

    @NotNull(message = " shouldn`t be null")
    private Long userId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long clubId;
}
