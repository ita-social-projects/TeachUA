package com.softserve.teachua.dto.child;

import com.softserve.teachua.constants.Sex;
import com.softserve.teachua.dto.marker.Convertible;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SuccessCreatedChild implements Convertible {
    private Long id;
    private String firstName;
    private String lastName;
    private Short age;
    private Sex sex;
}
