package com.softserve.teachua.controller;

import com.softserve.teachua.controller.marker.Api;
import com.softserve.teachua.dto.metric_item.Metric;
import com.softserve.teachua.service.impl.PrometheusServiceImpl;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometheusController implements Api {

    private final PrometheusServiceImpl prometheusService;

    public PrometheusController(PrometheusServiceImpl prometheusService) {
        this.prometheusService = prometheusService;
    }

    /**
     * Use this endpoint to get a report from Prometheus. The controller returns {@code String}.
     *
     * @return new {@code String}.
     */
    @GetMapping(path = "/prometheus", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getPrometheusMetrics() {
        return prometheusService.getPrometheusMetrics();
    }

    /**
     * Use this endpoint to get a report from Prometheus in a list form of Key and Values.
     * The controller returns {@code List<Metric>}.
     *
     * @return new {@code List<Metric>}.
     */
    @GetMapping(path = "/prometheus1")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Metric> getKeysAndValues() {
        return prometheusService.getKeysAndValues();
    }
}
