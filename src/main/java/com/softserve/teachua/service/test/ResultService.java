package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;

public interface ResultService {
    SuccessCreatedResult addResult(CreateResult result);
}
