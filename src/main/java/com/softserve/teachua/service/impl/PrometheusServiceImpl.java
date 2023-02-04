package com.softserve.teachua.service.impl;

import com.softserve.teachua.dto.metric_item.Metric;
import com.softserve.teachua.service.PrometheusService;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
@Slf4j
public class PrometheusServiceImpl implements PrometheusService {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    public PrometheusServiceImpl(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    @Override
    public String getPrometheusMetrics() {
        return prometheusMeterRegistry.scrape().replaceAll("# HELP", "\n# HELP");
    }

    @Override
    public List<Metric> getKeysAndValues() {
        ArrayList<Metric> metricsList = new ArrayList<>(50);
        // here we scrape metrics, and format them, to get it line by line
        String metrics = prometheusMeterRegistry.scrape().replaceAll("# HELP", "\n# HELP");

        // read String line by line to separate metrics as keys and values because we get metrics as plain text
        Scanner scanner = new Scanner(metrics);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // if line is comment or is empty, we don't store it
            if (!line.startsWith("#") && !line.trim().isEmpty()) {
                Metric currentMetric = new Metric();
                // negative look ahead regex, finds first space from the end of String
                String[] keyAndValue = line.split("( )(?!.* )");
                if (keyAndValue.length >= 2) {
                    currentMetric.setName(keyAndValue[0]);
                    currentMetric.setValue(keyAndValue[1]);
                    metricsList.add(currentMetric);
                }
            }
        }
        scanner.close();

        return metricsList;
    }
}
