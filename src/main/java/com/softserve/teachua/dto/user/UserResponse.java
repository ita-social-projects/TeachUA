package com.softserve.teachua.dto.user;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResponse implements Convertible {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String roleName;
}
