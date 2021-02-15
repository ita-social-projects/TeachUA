package com.softserve.teachua.dto.feedback;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackProfile implements Convertible {
    private Long id;

    @NotEmpty
    private String userName;

    @NotNull
    private Float rate;

    @NotEmpty
    private String text;

    @NotNull
    private Long userId;

    @NotNull
    private Long clubId;
}
