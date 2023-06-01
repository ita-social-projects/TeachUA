package com.softserve.teachua.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RefreshTokenResponse {
    String accessToken;
    String refreshToken;
}
