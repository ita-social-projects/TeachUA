package com.softserve.teachua.service.test.impl;

import com.softserve.teachua.model.User;
import com.softserve.teachua.model.test.Result;
import com.softserve.teachua.model.test.Test;
import com.softserve.teachua.repository.test.ResultRepository;
import com.softserve.teachua.service.test.ResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class ResultServiceImpl implements ResultService {
    private final ResultRepository resultRepository;

    public List<Result> findResultsByTest(Test test){
        return resultRepository.findResultsByTest(test);
    }

    public List<Result> findResultsByUser(User user){
        return resultRepository.findResultsByUser(user);
    }
}
