package dev.rohitahuja.metrics;

import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class ApplicationMetrics {

    private static final String JOB_LABEL_MEAL = "meal";
    private static final Map<String, Gauge> _meters = Map.of();
    private static String _jobStatusMeterKey = null;
    private static String _jobDurationMeterKey = null;
    private static final ThreadLocal<Instant> _lastSampledInstant = new ThreadLocal<>();
    private static String _feedName = null;
    private static String _jobType = null;

    private static void initializeKeysAndLabels(String jobName) {
        _jobStatusMeterKey = String.join(jobName, JobMeters.JOB_STATUS.getName());
        _jobDurationMeterKey = String.join(jobName, JobMeters.JOB_DURATION.getName());
    }

    public static void metersCreate(final String jobName) {
        _lastSampledInstant.set(Instant.now());
        identifyFeedNameAndJobType(jobName);
        initializeKeysAndLabels(_feedName);
        Gauge jobStatus = Gauge.builder()
                .name(_jobStatusMeterKey)
                .help("Status of the job")
                .labelNames(JOB_LABEL_MEAL)
                .register();
        _meters.put(_jobStatusMeterKey, jobStatus);

        Gauge jobDuration = Gauge.builder()
                .name(_jobDurationMeterKey)
                .help("Duration of the job")
                .labelNames(JOB_LABEL_MEAL)
                .unit(Unit.SECONDS)
                .register();
        _meters.put(_jobDurationMeterKey, jobDuration);
        jobRunning();
    }


    public static void jobRunning() {
        Gauge statusGauge = _meters.get(_jobStatusMeterKey);
        statusGauge.labelValues(_jobType).set(JobStatus.RUNNING.getStatus());

        Gauge durationGauge = _meters.get(_jobDurationMeterKey);
        long seconds = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        _lastSampledInstant.set(Instant.now());
        durationGauge.labelValues(_jobType).set(seconds);

    }

    public static void jobCompleted() {
        _meters.get(_jobStatusMeterKey).labelValues(_jobType).set(JobStatus.COMPLETED.getStatus());

        long seconds = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        _lastSampledInstant.set(Instant.now());
        _meters.get(_jobDurationMeterKey).labelValues(_jobType).set(seconds);
    }

    public static void jobFailed() {
        _meters.get(_jobStatusMeterKey).labelValues(_jobType).set(JobStatus.FAILED.getStatus());

        long seconds = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        _lastSampledInstant.set(Instant.now());
        _meters.get(_jobDurationMeterKey).labelValues(_jobType).set(seconds);
    }

    public static void metersReset(final String jobName) {
        _meters.get(_jobDurationMeterKey).labelValues(_jobType).set(0);
    }

    private static void identifyFeedNameAndJobType(String jobName) {
        String[] parts = jobName.split("_", 2);
        _feedName = parts.length > 0 ? parts[0] : "";
        _jobType = parts.length > 1 ? parts[1] : "";
    }


}
