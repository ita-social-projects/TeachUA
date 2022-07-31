package com.softserve.teachua.service.test;

import com.softserve.teachua.dto.test.result.CreateResult;
import com.softserve.teachua.dto.test.result.SuccessCreatedResult;
import com.softserve.teachua.model.test.Result;

public interface ResultService {
    SuccessCreatedResult addResult(CreateResult result);
    Result findById(Long id);
}
