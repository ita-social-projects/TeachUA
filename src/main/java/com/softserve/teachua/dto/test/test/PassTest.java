package com.softserve.teachua.dto.test.test;

import com.softserve.teachua.dto.test.question.PassingTestQuestion;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class PassTest {
    private String title;
    private List<PassingTestQuestion> questions;
}
