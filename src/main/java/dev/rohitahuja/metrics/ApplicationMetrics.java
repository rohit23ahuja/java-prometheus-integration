package dev.rohitahuja.metrics;

import dev.rohitahuja.util.CurrentJobInfo;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ApplicationMetrics {

    private static final String JOB_LABEL_MEAL = "meal";
    private static Map<String, Gauge> _meters = new HashMap<>();
    private static String _jobStatusMeterKey = null;
    private static String _jobDurationMeterKey = null;
    private static String _jobType = null;
    private static final ThreadLocal<Instant> _lastSampledInstant = new ThreadLocal<>();


    private static void initializeKeysAndLabels() {
        _jobStatusMeterKey = String.join(CurrentJobInfo.feedName(), JobMeters.JOB_STATUS.getName());
        _jobDurationMeterKey = String.join(CurrentJobInfo.feedName(), JobMeters.JOB_DURATION.getName());
        _jobType = CurrentJobInfo.jobType();
    }

    public static void metersCreate(final String jobName) {
        _lastSampledInstant.set(Instant.now());
        initializeKeysAndLabels();
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

}
