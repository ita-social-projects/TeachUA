package com.softserve.teachua.config;

import com.softserve.teachua.service.VersionService;
import com.softserve.teachua.service.impl.PropertiesServiceImpl;
import com.softserve.teachua.service.impl.VersionServiceImpl;
import org.junit.jupiter.api.*;

public class VersionCreateTest {

    @Test
    public void createVersion() {
        VersionService versionService = new VersionServiceImpl(new PropertiesServiceImpl());
        versionService.setVersion();
    }
}
