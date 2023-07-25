package com.softserve.teachua.dto.message;

import com.softserve.teachua.dto.marker.Convertible;
import jakarta.validation.constraints.NotNull;
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
public class MessageUpdateIsAnswered implements Convertible {
    @NotNull(message = " can't be null")
    private Boolean isAnswered;
}
