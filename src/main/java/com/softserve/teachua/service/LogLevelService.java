package com.softserve.teachua.service;

import com.softserve.teachua.utils.LogLevel;

public interface LogLevelService {
    void changeLogLevel(LogLevel logLevel, String packagePath);
}
