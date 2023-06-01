package com.softserve.teachua.dto.test.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class UserResult extends RepresentationModel<UserResult> {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime testFinishTime;
    private int grade;
}
