package com.softserve.teachua.dto.security;

import com.softserve.teachua.dto.marker.Convertible;
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

}
