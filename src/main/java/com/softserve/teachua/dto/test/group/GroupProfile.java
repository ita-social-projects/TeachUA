package com.softserve.teachua.dto.test.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
public class GroupProfile extends RepresentationModel<GroupProfile> {
    private String title;
    private String enrollmentKey;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
