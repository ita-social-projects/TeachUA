package com.softserve.teachua.service;

import com.softserve.teachua.dto.metric_item.Metric;
import java.util.List;

public interface PrometheusService {

    String getPrometheusMetrics();

    List<Metric> getKeysAndValues();
}
