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
    @NotNull(message = "cannot equal Null")
    private Long id;

    @NotEmpty
    @Length(min = 30,max = 1500,message = "Complaint size should be between 20 and 1500")
    private String text;

    @NotNull
    private Long userId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long clubId;
}
