package dev.rohitahuja.metrics;

import dev.rohitahuja.util.CurrentJobInfo;
import io.prometheus.metrics.core.datapoints.Timer;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.core.metrics.Histogram;
import io.prometheus.metrics.core.metrics.Summary;
import io.prometheus.metrics.model.snapshots.Unit;

import java.time.Duration;
import java.time.Instant;

public class BatchJobMetricsSingleApproach {
    private final Instant startTime;
    private final String feedName;
    private final String jobType;

    // Metrics
    private static final Histogram batchJobDuration = Histogram.builder()
            .name("batch_job_duration_seconds")
            .help("Duration of batch job execution in seconds")
            .labelNames("feed_name", "job_type", "status")
            //.buckets(1, 5, 10, 30, 60, 120, 300, 600, 1200, 1800) // 1s to 30min buckets. default buckets
            .register();


    private static final Counter batchJobExecutions = Counter.builder()
            .name("batch_job_executions_total")
            .help("Total number of batch job executions")
            .labelNames("feed_name", "job_type", "status")
            .register();

    private static final Gauge batchJobLastSuccess = Gauge.builder()
            .name("batch_job_last_success_timestamp")
            .help("Timestamp of last successful job execution")
            .labelNames("feed_name", "job_type")
            .unit(Unit.SECONDS)
            .register();

    private static final Gauge batchJobInfo = Gauge.builder()
            .name("batch_job_name")
            .help("Job information metadata")
            .labelNames("feed_name", "job_type")
            .register();

    public BatchJobMetricsSingleApproach(String jobName){
        this.startTime = Instant.now();
        String[] parts = jobName.split("_", 2);
        this.feedName = parts.length > 0 ? parts[0] : "";
        this.jobType = parts.length > 1 ? parts[1] : "";
        // Set job info metric
        batchJobInfo.labelValues(
                feedName,
                jobType
        ).set(1);

    }

    public void jobCompleted() {
        batchJobLastSuccess.labelValues(
                feedName,
                jobType)
                .set(Unit.millisToSeconds(System.currentTimeMillis()));

        batchJobDuration.labelValues(
                feedName,
                jobType,
                JobStatus.COMPLETED.getLabel())
                .observe(Duration.between(startTime, Instant.now()).toMillis() / 1000.0);
        batchJobExecutions.labelValues(
                feedName,
                jobType,
                JobStatus.COMPLETED.getLabel())
                .inc();
    }

    public void jobFailed() {
        batchJobDuration.labelValues(
                feedName,
                jobType,
                JobStatus.FAILED.getLabel())
                .observe(Duration.between(startTime, Instant.now()).toMillis() / 1000.0);
        batchJobExecutions.labelValues(
                        feedName,
                        jobType,
                        JobStatus.FAILED.getLabel())
                .inc();
    }
}
