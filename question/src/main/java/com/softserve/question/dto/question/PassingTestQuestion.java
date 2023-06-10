package com.softserve.question.dto.question;

import com.softserve.question.dto.answer.PassingTestAnswer;
import java.util.List;
import lombok.Data;

@Data
public class PassingTestQuestion {
    private String title;
    private String description;
    private List<PassingTestAnswer> answers;
}
