package com.softserve.teachua.dto.security;

import com.softserve.teachua.dto.marker.Dto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserEntity implements Dto {

    private Long id;
    private String email;
    private String password;
    private String roleName;

}
