package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FeedbackProfile implements Convertible {
    @NotNull(message = "cannot be Null")
    private Long id;

    @NotNull
    @Min( value = 0,message = "Rate cannot be  less than 0")
    @Max(value = 5,message = "Rate cannot be more than 5 ")
    private Float rate;

    @NotNull
    @NotEmpty
    @Size(min = 10,max = 1500,message = "field should be between 10 and 1500 symbols")
    @Pattern(regexp = "[a-zA-zа-яА-Я0-9іІїЇєЄґҐ!.,№;&'`~{}%:?*()_+=\"\\[\\]\\\\\\/\\-\\s&&[^ёЁъЁыЫэЭ]]+",
            message = "You can use Ukrainian,English alphabet and some special char like: !.,№;&'`~{}%:?*()_+=\"[]\\/-")
    private String text;

    @NotNull
    private Long userId;

    @NotNull
    private Long clubId;
}
