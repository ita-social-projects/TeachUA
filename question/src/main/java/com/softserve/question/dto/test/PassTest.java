package com.softserve.question.dto.test;

import com.softserve.question.dto.question.PassingTestQuestion;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PassTest {
    private String title;
    private List<PassingTestQuestion> questions;
}
