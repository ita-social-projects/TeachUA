package com.softserve.teachua.dto.test.result;

import lombok.Data;

import java.util.List;

@Data
public class CreateResult {
    private Long userId;
    private Long testId;
    private List<Long> selectedAnswers;
}
