package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.dto.test.test.SuccessCreatedTest;
import com.softserve.teachua.model.test.Test;

import java.util.List;

public interface TestService {
    SuccessCreatedTest addTest(CreateTest test);
    List<Test> findActiveTests();
    List<Test> findArchivedTests();
    List<Test> findUnarchivedTests();
    void archiveTestById(Long id);
}
