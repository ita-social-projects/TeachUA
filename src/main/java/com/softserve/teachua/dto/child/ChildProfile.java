package com.softserve.teachua.dto.child;

import com.softserve.teachua.constants.Gender;
import com.softserve.teachua.dto.marker.Convertible;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ChildProfile implements Convertible {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Range(min = 2, max = 18)
    private Short age;
    @NotNull
    private Gender gender;
}
