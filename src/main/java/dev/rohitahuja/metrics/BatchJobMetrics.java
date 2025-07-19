package dev.rohitahuja.metrics;

import dev.rohitahuja.util.CurrentJobInfo;
import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class BatchJobMetrics {
    private static final Logger _log = LoggerFactory.getLogger(BatchJobMetrics.class);
    private static final String LABEL_FEEDNAME = "feedname";
    private static final String LABEL_CAPTURED_AT = "capturedAt";
    private static final String LABEL_JOBTYPE = "jobtype";

    private final String feedName;
    private final String jobType;
    private Instant lastSampledInstant;
    private static Gauge jobStatus;
    private static Gauge jobDuration;
    private static Counter jobCompleted;
    private static Counter jobFailed;


    public BatchJobMetrics(String jobName) {
        lastSampledInstant = Instant.now();
        String[] parts = jobName.split("_", 2);
        this.feedName = parts.length > 0 ? parts[0] : "";
        this.jobType = parts.length > 1 ? parts[1] : "";

        jobStatus = Gauge.builder()
                .name(JobMeters.JOB_STATUS.getName())
                .help("Status of the job")
                .labelNames(LABEL_FEEDNAME, LABEL_JOBTYPE, LABEL_CAPTURED_AT)
                .register();

        jobDuration = Gauge.builder()
                .name(JobMeters.JOB_DURATION.getName())
                .help("Duration of the job")
                .labelNames(LABEL_FEEDNAME, LABEL_JOBTYPE)
                .unit(Unit.SECONDS)
                .register();

        jobCompleted = Counter.builder()
                .name(JobMeters.JOB_COMPLETED.getName())
                .labelNames(LABEL_FEEDNAME, LABEL_JOBTYPE)
                .help("Job completed")
                .register();

        jobFailed = Counter.builder()
                .name(JobMeters.JOB_FAILED.getName())
                .labelNames(LABEL_FEEDNAME, LABEL_JOBTYPE)
                .help("Job failed")
                .register();
    }


    public void jobRunning() {
        _log.debug("Job running metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a"));

        jobStatus.labelValues(feedName, jobType, currentUtcTime).set(JobStatus.RUNNING.getStatus());

        double currentDuration = jobDuration.labelValues(feedName, jobType).get();
        long elapsedSecondsSinceLastSample = Duration.between(lastSampledInstant, Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        lastSampledInstant = Instant.now();
        jobDuration.labelValues(feedName,jobType).set(total);

    }

    public void jobCompleted() {
        _log.debug("Job completed metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a"));
        jobStatus.labelValues(feedName, jobType, currentUtcTime).set(JobStatus.COMPLETED.getStatus());
        jobCompleted.labelValues(feedName, jobType).inc();
        jobFailed.labelValues(feedName, jobType).inc(0);


        double currentDuration = jobDuration.labelValues(feedName, jobType).get();
        long elapsedSecondsSinceLastSample = Duration.between(lastSampledInstant, Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        lastSampledInstant = Instant.now();
        jobDuration.labelValues(feedName,jobType).set(total);


    }

    public void jobFailed() {
        _log.error("Job failed metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a"));
        jobStatus.labelValues(feedName, jobType, currentUtcTime).set(JobStatus.FAILED.getStatus());
        jobFailed.labelValues(feedName, jobType).inc();
        jobCompleted.labelValues(feedName, jobType).inc(0);

        double currentDuration = jobDuration.labelValues(feedName, jobType).get();
        long elapsedSecondsSinceLastSample = Duration.between(lastSampledInstant, Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        lastSampledInstant= Instant.now();
        jobDuration.labelValues(feedName, jobType).set(total);
    }

}
