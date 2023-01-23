package com.softserve.teachua.service.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BadServiceTest {


    @Test
    void doNothing() {
        BadService badService = new BadService();
        String actual = badService.getNoting();
        assertNotNull(actual);
    }
}
