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
    @Length(min = 30,max = 255,message = "length exceeded available size")
    private String text;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long clubId;
}
