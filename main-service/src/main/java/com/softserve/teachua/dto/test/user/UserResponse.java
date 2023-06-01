package com.softserve.teachua.dto.test.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class UserResponse extends RepresentationModel<UserResponse> {
    private String firstName;
    private String lastName;
}
