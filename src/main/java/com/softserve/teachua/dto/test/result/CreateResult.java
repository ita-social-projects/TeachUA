package com.softserve.teachua.dto.test.result;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateResult {
    private Long testId;
    private List<Long> selectedAnswersIds;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime startTime;
}
