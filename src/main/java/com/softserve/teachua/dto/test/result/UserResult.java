package com.softserve.teachua.dto.test.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResult extends RepresentationModel<UserResult> {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime testFinishTime;
    private int grade;
}
