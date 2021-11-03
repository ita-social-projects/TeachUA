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

    @NotEmpty
    @Size(min = 10,max = 1500,message = " field:Відгук не може містити менше 10 символів та більше 1500 символів")
    @Pattern(regexp = "^[А-Яа-яіІєЄїЇґҐ\\'a-zA-Z0-9()\\\\!\\\"\\\"#$%&'*\\n+\\r, ,\\-.:;\\\\<=>—«»„”“–’‘?|@_`{}№~^/\\[\\]]+[^:эЭъЪыЫёЁ]$",
             message = "cannot contain Russian letters")
    private String text;

    @NotNull
    private Long userId;

    @NotNull
    private Long clubId;
}
