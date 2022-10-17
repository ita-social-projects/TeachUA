package com.softserve.teachua.dto.test.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public class UserResponse extends RepresentationModel<UserResponse> {
    private String firstName;
    private String lastName;
}
