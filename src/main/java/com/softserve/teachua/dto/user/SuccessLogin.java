package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Data
public class SuccessLogin implements Convertible {
    private Long id;
    private String email;
    private String roleName;
    private String accessToken;
    private String refreshToken;
}
