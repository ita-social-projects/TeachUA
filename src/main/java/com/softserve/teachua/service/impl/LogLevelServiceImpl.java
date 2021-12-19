package com.softserve.teachua.service.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.softserve.teachua.service.LogLevelService;
import com.softserve.teachua.utils.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogLevelServiceImpl implements LogLevelService {

    @Override
    public void changeLogLevel(final LogLevel logLevel, final String packageName) {
        final LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final Logger logger = loggerContext.getLogger(packageName);
        logger.setLevel(Level.toLevel(logLevel.name().toUpperCase()));
    }
}
