package com.softserve.teachua.dto.test.answer;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Data
public class AnswerResult {
    private String title;
    private boolean checked;
    private boolean correct;
}
