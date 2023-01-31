package com.softserve.teachua.dto.test.question;

import com.softserve.teachua.dto.test.answer.PassingTestAnswer;
import java.util.List;
import lombok.Data;

@Data
public class PassingTestQuestion {
    private String title;
    private String description;
    private List<PassingTestAnswer> answers;
}
