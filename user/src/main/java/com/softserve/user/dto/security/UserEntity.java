package com.softserve.user.dto.security;

import com.softserve.commons.util.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserEntity implements Convertible {
    private Long id;
    private String email;
    private String password;
    private String roleName;
    private boolean status;
}
