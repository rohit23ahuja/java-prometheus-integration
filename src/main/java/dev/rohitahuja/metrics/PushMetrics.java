package dev.rohitahuja.metrics;

import dev.rohitahuja.util.ConfigReader;
import io.prometheus.metrics.exporter.pushgateway.PushGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PushMetrics {
    private static final Logger _log = LoggerFactory.getLogger(PushMetrics.class);

    private static ScheduledExecutorService scheduler = null;
    public static void start(String jobName) {
        final PushGateway pushGateway = PushGateway.builder()
                .address(ConfigReader.get("metrics.pushgateway.url"))
                .job(ConfigReader.get("metrics.job.name"))
                .groupingKey("job_name", jobName)
                .build();

        scheduler = Executors.newScheduledThreadPool(ConfigReader.getInt("metrics.threadpool.size"));
        scheduler.scheduleAtFixedRate(() -> {
            try {
                pushGateway.pushAdd();
            } catch (IOException e) {
                _log.error("Error while pushing metrics: {}", e.getMessage(), e);
            }
        }, 0, ConfigReader.getInt("metrics.pushrate"), TimeUnit.SECONDS);

    }

    public static void stop() {
        scheduler.shutdown();
    }
}
