package com.softserve.teachua.dto.child;

import com.softserve.teachua.constants.Gender;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.ParentResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChildResponse implements Convertible {
    private Long id;
    private String firstName;
    private String lastName;
    private ParentResponse parentId;
    private Short age;
    private Gender gender;
}
