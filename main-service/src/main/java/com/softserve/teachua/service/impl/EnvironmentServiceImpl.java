package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.log.EnvironmentResponce;
import com.softserve.teachua.service.EnvironmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnvironmentServiceImpl implements EnvironmentService {
    public EnvironmentResponce getEnvironment(String environmentName) {
        String result = System.getenv().get(environmentName.toUpperCase());
        return new EnvironmentResponce(result == null ? "null" : result);
    }
}
