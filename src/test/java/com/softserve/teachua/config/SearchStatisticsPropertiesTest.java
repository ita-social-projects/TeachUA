package com.softserve.teachua.config;

import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

@EnableConfigurationProperties(SearchStatisticsProperties.class)
class SearchStatisticsPropertiesTest {

    private SearchStatisticsProperties properties;

    @BeforeEach
    public void setUp() {
        properties = new SearchStatisticsProperties();
    }

    @Test
    public void testToCache() {
        properties.setToCache(true);
        assertTrue(properties.isToCache());

        properties.setToCache(false);
        assertFalse(properties.isToCache());
    }

    @Test
    public void testMaxTime() {
        properties.setMaxTime("2023-10-09 12:00:00");
        assertEquals("2023-10-09 12:00:00", properties.getMaxTime());
    }

    @Test
    public void testMaxNumber() {
        properties.setMaxNumber(42);
        assertEquals(42, properties.getMaxNumber());
    }

    @Test
    public void testValidateAndParseMaxTimeValid() {
        properties.setMaxTime("2023-10-09 12:00:00");
        Date result = properties.validateAndParseMaxTime();
        assertNotNull(result);
    }

    @Test
    public void testValidateAndParseMaxTimeInvalid() {
        properties.setMaxTime("invalid-date-format");
        assertThrows(IllegalArgumentException.class, () -> {
            properties.validateAndParseMaxTime();
        });
    }

    @Test
    public void testValidateAndParseMaxTimeNull() {
        properties.setMaxTime("23324:323-122");
        assertThrows(IllegalArgumentException.class, () -> {
            properties.validateAndParseMaxTime();
        });
    }
}
