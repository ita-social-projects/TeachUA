package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.test.CreateTest;
import com.softserve.teachua.dto.test.test.SuccessCreatedTest;

public interface TestService {
    SuccessCreatedTest addTest(CreateTest test);
}
