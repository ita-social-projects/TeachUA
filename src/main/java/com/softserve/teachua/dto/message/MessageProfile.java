package com.softserve.teachua.dto.message;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Getter
@Setter
public class MessageProfile implements Convertible {

    private Long id;

    @NotNull(message = "clubId can't be null")
    private Long clubId;

    @CheckRussian
    @Size(min = 10, max = 1500, message = " should be between 10 and 1500 symbols")
    private String text;

    @NotNull(message = "senderId can't be null")
    private Long senderId;

    @NotNull(message = "recipientId can't be null")
    private Long recipientId;

    private Boolean isActive;
}
