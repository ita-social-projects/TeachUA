package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SuccessUpdatedUser implements Dto {
    private String name;
    private Long id;
    private String email;
}
