package com.softserve.teachua.dto.test.result;

import com.softserve.commons.util.marker.Convertible;
import java.util.List;
import lombok.Data;

@Data
public class SuccessCreatedResult implements Convertible {
    private Long userId;
    private Long testId;
    private List<Long> selectedAnswersIds;
    private int grade;
}
