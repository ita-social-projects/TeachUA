package com.softserve.teachua.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
}
