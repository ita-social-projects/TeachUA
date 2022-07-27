package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.repository.test.QuestionTypeRepository;
import com.softserve.teachua.service.test.QuestionTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class QuestionTypeServiceImpl implements QuestionTypeService {
    private final QuestionTypeRepository questionTypeRepository;
}
