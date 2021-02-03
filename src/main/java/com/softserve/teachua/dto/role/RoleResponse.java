package com.softserve.teachua.dto.role;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleResponse implements Convertible {

    private Integer id;
    private String roleName;
}
