package com.softserve.teachua.dto.test.test;

import com.softserve.teachua.dto.test.question.PassingTestQuestion;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PassTest {
    private String title;
    private List<PassingTestQuestion> questions;
}
