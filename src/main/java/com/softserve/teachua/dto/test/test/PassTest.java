package com.softserve.teachua.dto.test.test;

import com.softserve.teachua.dto.test.question.PassingTestQuestion;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PassTest {
    private String title;
    private List<PassingTestQuestion> questions;
}
