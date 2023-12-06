package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReplyRequest implements Convertible {
    private Long id;
    private Long parentCommentId;
    @CheckRussian
    @Size(min = 10, max = 1000, message = " should be between 10 and 1000 symbols")
    private String text;
    @NotNull
    @Min(0)
    private Long userId;
}
