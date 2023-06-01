package com.softserve.teachua.service;

import com.softserve.teachua.dto.log.EnvironmentResponce;

public interface EnvironmentService {
    EnvironmentResponce getEnvironment(String environmentName);
}
