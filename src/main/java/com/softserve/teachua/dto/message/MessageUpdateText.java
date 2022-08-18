package com.softserve.teachua.dto.message;

import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.utils.validations.CheckRussian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@With
@Getter
@Setter
public class MessageUpdateText implements Convertible {

    @CheckRussian
    @Size(min = 1, max = 1000, message = " should be between 1 and 1000 symbols")
    private String text;
}
