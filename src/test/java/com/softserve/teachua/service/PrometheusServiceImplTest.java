package com.softserve.teachua.service;

import com.softserve.teachua.dto.metric_item.Metric;
import com.softserve.teachua.service.impl.PrometheusServiceImpl;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PrometheusServiceImplTest {

    @Mock
    PrometheusMeterRegistry prometheusMeterRegistry;

    @InjectMocks
    PrometheusServiceImpl prometheusService;

    private final String CORRECT_METRIC = "system_cpu_count 12.0";
    private final String CORRECT_METRIC_NAME = "system_cpu_count";
    private final String CORRECT_METRIC_VALUE = "12.0";
    private final String CORRECT_COMPLEX_METRIC = "jvm_gc_pause_seconds_max{action=\"end of minor GC\",cause=\"Metadata GC Threshold\",} 0.008";
    private final String CORRECT_COMPLEX_METRIC_NAME = "jvm_gc_pause_seconds_max{action=\"end of minor GC\",cause=\"Metadata GC Threshold\",}";
    private final String CORRECT_COMPLEX_VALUE = "0.008";
    private final String METRIC_WITH_COMMENT = "# HELP jvm_gc_pause_seconds_max Time spent in GC pause";

    @Test
    void getPrometheusMetricsShouldReturnCorrectMetrics() {
        when(prometheusMeterRegistry.scrape()).thenReturn(CORRECT_METRIC);
        assertThat(prometheusService.getPrometheusMetrics()).isEqualTo(CORRECT_METRIC);
    }

    @Test
    void getKeysAndValuesFromCorrectMetricShouldReturnCorrectMetric() {
        Metric expectedMetric = new Metric(CORRECT_METRIC_NAME, CORRECT_METRIC_VALUE);
        when(prometheusMeterRegistry.scrape()).thenReturn(CORRECT_METRIC);
        assertTrue(prometheusService.getKeysAndValues().contains(expectedMetric));
    }

    @Test
    void getKeysAndValuesFromCorrectMetricAndComplexMetricShouldReturnCorrectMetrics() {
        Metric expectedMetric = new Metric(CORRECT_METRIC_NAME, CORRECT_METRIC_VALUE);
        Metric expectedComplexMetric = new Metric(CORRECT_COMPLEX_METRIC_NAME, CORRECT_COMPLEX_VALUE);

        String registryDataWithTwoMetrics = CORRECT_METRIC + "\n" + CORRECT_COMPLEX_METRIC;

        when(prometheusMeterRegistry.scrape()).thenReturn(registryDataWithTwoMetrics);
        assertTrue(prometheusService.getKeysAndValues().contains(expectedMetric));
        assertTrue(prometheusService.getKeysAndValues().contains(expectedComplexMetric));
    }

    @Test
    void getKeysAndValuesFromCorrectMetricAndComplexMetricAndCommenShouldReturnCorrectMetricsWithoutComment() {
        Metric expectedMetric = new Metric(CORRECT_METRIC_NAME, CORRECT_METRIC_VALUE);
        Metric expectedComplexMetric = new Metric(CORRECT_COMPLEX_METRIC_NAME, CORRECT_COMPLEX_VALUE);

        String registryDataWithTwoMetricsAndComment = CORRECT_METRIC + "\n" + METRIC_WITH_COMMENT + "\n" + CORRECT_COMPLEX_METRIC;

        when(prometheusMeterRegistry.scrape()).thenReturn(registryDataWithTwoMetricsAndComment);
        assertTrue(prometheusService.getKeysAndValues().contains(expectedMetric));
        assertTrue(prometheusService.getKeysAndValues().contains(expectedComplexMetric));
        assertEquals(2, prometheusService.getKeysAndValues().size());
    }
}
