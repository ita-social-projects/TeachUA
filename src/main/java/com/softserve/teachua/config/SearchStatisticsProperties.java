package com.softserve.teachua.config;

import jakarta.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @PostConstruct
    public Date validateAndParseMaxTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(this.maxTime);
        } catch (ParseException | IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }
}
