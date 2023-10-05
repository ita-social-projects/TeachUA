package com.softserve.teachua.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "statistics")
public class SearchStatisticsProperties {
    private boolean toCache;
    private String maxTime;
    private int maxNumber;
}
