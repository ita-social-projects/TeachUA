package com.softserve.teachua.dto.complaint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ComplaintProfile {
    private Long id;

    @NotNull
    private Float rate;

    @NotEmpty
    private String text;

    @NotNull
    private Long userId;

    @NotNull
    private Long clubId;
}
