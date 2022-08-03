package com.softserve.teachua.dto.test.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
public class UserResult extends RepresentationModel<UserResult> {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime testFinishTime;
    private int grade;
}
