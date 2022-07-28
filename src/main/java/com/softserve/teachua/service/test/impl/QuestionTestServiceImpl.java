package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.test.Question;
import com.softserve.teachua.model.test.QuestionTest;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.test.QuestionTestRepository;
import com.softserve.teachua.service.test.QuestionTestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionTestServiceImpl implements QuestionTestService {
    private final QuestionTestRepository questionTestRepository;

    public List<Question> findQuestionsByTest(Test test){
        List<QuestionTest> questionTests = questionTestRepository.findQuestionTestsByTest(test);

        return questionTests.stream().map(QuestionTest::getQuestion).collect(Collectors.toList());
    }
}
