package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String getActuatorInfo() {
        return prometheusMeterRegistry.scrape().replaceAll("# HELP", "\n# HELP");
    }

    /**
     * Use this endpoint to get a report from Prometheus. The controller returns {@code String}.
     *
     * @return new {@code String}.
     */
    @GetMapping(path = "/prometheus1", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getKeysAndValues() {
        String metrics = prometheusMeterRegistry.scrape().replaceAll("# HELP", "\n# HELP");

        StringBuilder sb = new StringBuilder();

        // read String line by line to separate metrics as keys and values
        Scanner scanner = new Scanner(metrics);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // if line is comment or is empty, we don't store it
            if(!line.startsWith("#") && !line.trim().isEmpty()){
                String[] keyAndValue = line.split(" ");
                if(keyAndValue.length >= 2){
                    for (int i = 0; i < keyAndValue.length - 1; i++) {
                        sb.append(keyAndValue[i]);
                    }
                    sb.append(" ").append(keyAndValue[keyAndValue.length - 1]).append("\n");
                }
            }
        }
        scanner.close();
        return sb.toString();
    }
}
