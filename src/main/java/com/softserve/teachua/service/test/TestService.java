package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.dto.test.test.ResultTest;
import com.softserve.teachua.dto.test.test.SuccessCreatedTest;
import com.softserve.teachua.model.test.Test;

import java.util.List;

public interface TestService {
    SuccessCreatedTest addTest(CreateTest test);
    List<Test> findActiveTests();
    List<Test> findArchivedTests();
    List<Test> findUnarchivedTests();
    Test findById(Long id);
    void archiveTestById(Long id);
    ResultTest getResultTest(Long testId, Long resultId);
    public SuccessCreatedResult saveResult(CreateResult resultDto);
}
