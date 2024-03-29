package com.softserve.teachua.dto.child;

//import com.softserve.teachua.constants.Gender;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.dto.user.ParentResponse;
import com.softserve.teachua.model.Gender;
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
    private ParentResponse parent;
    private Short age;
    private Gender gender;
    private boolean isDisabled = false;
}
