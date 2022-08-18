package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class SuccessLogout implements Convertible {
    private Long id;
    private String email;
    private String accessToken;
}
