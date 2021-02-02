package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@AllArgsConstructor
@Builder
@Data
public class UserProfile implements Dto {
    @NotEmpty
    private String email;

    @NotEmpty
    private String name;

    @NotEmpty
    private String password;
}
