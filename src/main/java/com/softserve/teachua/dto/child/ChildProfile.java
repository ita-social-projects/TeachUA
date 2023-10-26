package com.softserve.teachua.dto.child;

//import com.softserve.teachua.constants.Gender;
import com.softserve.teachua.dto.marker.Convertible;
import com.softserve.teachua.model.Gender;
import com.softserve.teachua.utils.validations.CheckRussian;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChildProfile implements Convertible {
    @NotBlank
    @CheckRussian
    private String firstName;
    @NotBlank
    @CheckRussian
    private String lastName;
    @Range(min = 2, max = 18)
    private Short age;
    @NotNull
    private String gender;
}
