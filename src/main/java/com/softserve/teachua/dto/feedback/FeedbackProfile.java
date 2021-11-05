package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackProfile implements Convertible {
    private Long id;

    @NotNull
    @Min( value = 0,message = "Rate cannot be  less than 0")
    @Max(value = 5,message = "Rate cannot be more than 5 ")
    private Float rate;


    @NotEmpty
    @Length(min = 10,max = 1500,message = " should be between 10 and 1500 symbols")
    private String text;

    @NotNull(message = " shouldn`t be null")
    private Long userId;

    @NotNull
    private Long clubId;
}
