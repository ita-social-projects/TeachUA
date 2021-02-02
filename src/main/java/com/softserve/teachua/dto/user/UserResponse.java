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
public class UserResponse implements Dto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String roleName;
}
