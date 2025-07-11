package dev.rohitahuja.metrics;

import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ApplicationMetricsGenericBatchJob {
    private static final Logger _log = LoggerFactory.getLogger(ApplicationMetricsGenericBatchJob.class);
    private static final String UNDERSCORE = "_";
    private static final String LABEL_JOBNAME = "jobname";
    private static final String LABEL_LAST_EXECUTION_TIME = "lastExecutionTime";

    private static Map<String, Gauge> _meters = new HashMap<>();
    private static String _jobStatusMeterKey = null;
    private static String _jobDurationMeterKey = null;
    private static String _jobName = null;

    private static final ThreadLocal<Instant> _lastSampledInstant = new ThreadLocal<>();


    private static void initializeKeysAndLabels(String jobName) {
        _jobStatusMeterKey = String.join(UNDERSCORE, jobName, JobMeters.JOB_STATUS.getName());
        _jobDurationMeterKey = String.join(UNDERSCORE, jobName, JobMeters.JOB_DURATION.getName());
        _jobName = jobName;
    }

    public static void metersCreate(String jobName) {
        _lastSampledInstant.set(Instant.now());
        initializeKeysAndLabels(jobName);
        Gauge jobStatus = Gauge.builder()
                .name(_jobStatusMeterKey)
                .help("Status of the job")
                .labelNames(LABEL_JOBNAME, LABEL_LAST_EXECUTION_TIME)
                .register();
        _meters.put(_jobStatusMeterKey, jobStatus);

        Gauge jobDuration = Gauge.builder()
                .name(_jobDurationMeterKey)
                .help("Duration of the job")
                .labelNames(LABEL_JOBNAME)
                .unit(Unit.SECONDS)
                .register();
        _meters.put(_jobDurationMeterKey, jobDuration);
        jobRunning();
    }

    public static void jobRunning() {
        _log.info("Job running metrics");

        _meters.get(_jobStatusMeterKey)
                .labelValues(_jobName, "")
                .set(JobStatus.RUNNING.getStatus());

        Gauge durationGauge = _meters.get(_jobDurationMeterKey);
        double currentDuration = durationGauge.labelValues(_jobName).get();
        long elapsedSecondsSinceLastSample = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        _lastSampledInstant.set(Instant.now());
        durationGauge.labelValues(_jobName).set(total);

    }

    public static void jobCompleted() {
        _log.info("Job completed metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a 'UTC'"));
        _meters.get(_jobStatusMeterKey)
                .labelValues(_jobName, currentUtcTime)
                .set(JobStatus.COMPLETED.getStatus());

        Gauge durationGauge = _meters.get(_jobDurationMeterKey);
        double currentDuration = durationGauge.labelValues(_jobName).get();
        long elapsedSecondsSinceLastSample = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        _lastSampledInstant.set(Instant.now());
        durationGauge.labelValues(_jobName).set(total);
    }

    public static void jobFailed() {
        _log.info("Job failed metrics");

        String currentUtcTime = Instant.now()
                .atZone(ZoneOffset.UTC)
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy h:mm:ss a 'UTC'"));
        _meters.get(_jobStatusMeterKey)
                .labelValues(_jobName, currentUtcTime)
                .set(JobStatus.FAILED.getStatus());

        Gauge durationGauge = _meters.get(_jobDurationMeterKey);
        double currentDuration = durationGauge.labelValues(_jobName).get();
        long elapsedSecondsSinceLastSample = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        _lastSampledInstant.set(Instant.now());
        durationGauge.labelValues(_jobName).set(total);
    }
}
