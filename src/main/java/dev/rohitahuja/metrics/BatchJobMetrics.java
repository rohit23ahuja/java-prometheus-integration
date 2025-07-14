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
    private static final String UNDERSCORE = "_";
    private static final String LABEL_JOBNAME = "jobname";
    private static final String LABEL_CAPTURED_AT = "capturedAt";

    private final ThreadLocal<Instant> lastSampledInstant;
    private final Gauge jobStatus;
    private final Gauge jobDuration;
    private final Counter jobCompleted;
    private final Counter jobFailed;
    private final String jobName;

    public BatchJobMetrics(String jobName) {
        lastSampledInstant = new ThreadLocal<>();
        lastSampledInstant.set(Instant.now());
        this.jobName = jobName;

        jobStatus = Gauge.builder()
                .name(String.join(UNDERSCORE, jobName, JobMeters.JOB_STATUS.getName()))
                .help("Status of the job")
                .labelNames(LABEL_JOBNAME, LABEL_CAPTURED_AT)
                .register();

        jobDuration = Gauge.builder()
                .name(String.join(UNDERSCORE, jobName, JobMeters.JOB_DURATION.getName()))
                .help("Duration of the job")
                .labelNames(LABEL_JOBNAME)
                .unit(Unit.SECONDS)
                .register();

        jobCompleted = Counter.builder()
                .name(String.join(UNDERSCORE, jobName, JobMeters.JOB_COMPLETED.getName()))
                .labelNames(LABEL_JOBNAME)
                .help("Job completed")
                .register();

        jobFailed = Counter.builder()
                .name(String.join(UNDERSCORE, jobName, JobMeters.JOB_FAILED.getName()))
                .labelNames(LABEL_JOBNAME)
                .help("Job failed")
                .register();
    }


    public void jobRunning() {
        _log.debug("Job running metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a"));

        jobStatus.labelValues(jobName, currentUtcTime).set(JobStatus.RUNNING.getStatus());

        double currentDuration = jobDuration.labelValues(jobName).get();
        long elapsedSecondsSinceLastSample = Duration.between(lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        lastSampledInstant.set(Instant.now());
        jobDuration.labelValues(jobName).set(total);

    }

    public void jobCompleted() {
        _log.debug("Job completed metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a"));
        jobStatus.labelValues(jobName, currentUtcTime).set(JobStatus.COMPLETED.getStatus());
        jobCompleted.labelValues(jobName).inc();
        jobFailed.labelValues(jobName).inc(0);


        double currentDuration = jobDuration.labelValues(jobName).get();
        long elapsedSecondsSinceLastSample = Duration.between(lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        lastSampledInstant.set(Instant.now());
        jobDuration.labelValues(jobName).set(total);


    }

    public void jobFailed() {
        _log.error("Job failed metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a"));
        jobStatus.labelValues(jobName, currentUtcTime).set(JobStatus.FAILED.getStatus());
        jobFailed.labelValues(jobName).inc();
        jobCompleted.labelValues(jobName).inc(0);

        double currentDuration = jobDuration.labelValues(jobName).get();
        long elapsedSecondsSinceLastSample = Duration.between(lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        lastSampledInstant.set(Instant.now());
        jobDuration.labelValues(jobName).set(total);
    }

}
