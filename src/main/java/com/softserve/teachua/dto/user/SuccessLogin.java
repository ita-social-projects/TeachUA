package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class SuccessLogin implements Dto {

    private Long id;
    private String email;
    private String accessToken;

}
