package com.softserve.teachua.dto.message;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Getter
@Setter
public class MessageProfile implements Convertible {
    private Long id;

    @NotNull(message = " can't be null")
    private Long clubId;

    @CheckRussian
    @Size(min = 1, max = 1000, message = " should be between 1 and 1000 symbols")
    private String text;

    @NotNull(message = " can't be null")
    private Long senderId;

    @NotNull(message = " can't be null")
    private Long recipientId;

    private Boolean isActive;

    private Boolean isAnswered;
}
