package com.softserve.teachua.dto.test.result;

import com.softserve.teachua.dto.marker.Convertible;
import lombok.Data;

import java.util.List;

@Data
public class SuccessCreatedResult implements Convertible {
    private Long userId;
    private Long testId;
    private List<Long> selectedAnswersIds;
    private int grade;
}
