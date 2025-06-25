package dev.rohitahuja.metrics;

import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.exporter.pushgateway.PushGateway;
import io.prometheus.metrics.model.snapshots.Unit;

public class MetricsManager {
    public static PushGateway pushGateway = PushGateway.builder()
            .address("localhost:9091") // not needed as localhost:9091 is the default
            .job("java-prometheus-integration")
            .build();

    public static Gauge itemsProcessedCount = Gauge.builder()
            .name("items_processed")
            .help("items processed in the last batch job run")
            .register();





}
