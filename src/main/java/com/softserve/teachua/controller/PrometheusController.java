package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.metric_item.Metric;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
public class PrometheusController implements Api {

    private final PrometheusMeterRegistry prometheusMeterRegistry;

    public PrometheusController(PrometheusMeterRegistry prometheusMeterRegistry) {
        this.prometheusMeterRegistry = prometheusMeterRegistry;
    }

    /**
     * Use this endpoint to get a report from Prometheus. The controller returns {@code String}.
     *
     * @return new {@code String}.
     */
    @GetMapping(path = "/prometheus", produces = MediaType.TEXT_PLAIN_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public String getActuatorInfo() {
        return prometheusMeterRegistry.scrape().replaceAll("# HELP", "\n# HELP");
    }

    /**
     * Use this endpoint to get a report from Prometheus in a list form. The controller returns {@code List<Metric>}.
     *
     * @return new {@code List<Metric>}.
     */
    @GetMapping(path = "/prometheus1")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Metric> getKeysAndValues() {
        ArrayList<Metric> metricsList = new ArrayList<>(50);
        String metrics = prometheusMeterRegistry.scrape().replaceAll("# HELP", "\n# HELP");

        // read String line by line to separate metrics as keys and values because we get metrics as plain text
        Scanner scanner = new Scanner(metrics);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // if line is comment or is empty, we don't store it
            if (!line.startsWith("#") && !line.trim().isEmpty()) {
                Metric currentMetric = new Metric();
                String[] keyAndValue = line.split(" ");
                if (keyAndValue.length >= 2) {

                    StringBuilder sb = new StringBuilder();
                    // get full name of complex metric name
                    for (int i = 0; i < keyAndValue.length - 1; i++) {
                        sb.append(keyAndValue[i]);
                    }
                    currentMetric.setName(sb.toString());

                    // get the value of given metric
                    currentMetric.setValue(keyAndValue[keyAndValue.length - 1]);
                    metricsList.add(currentMetric);
                }
            }
        }
        scanner.close();

        return metricsList;
    }
}
