package dev.rohitahuja.metrics;

import dev.rohitahuja.batchjobs.FruitBatchJob;
import dev.rohitahuja.util.CurrentJobInfo;
import io.prometheus.metrics.core.metrics.Gauge;
import io.prometheus.metrics.model.snapshots.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class ApplicationMetrics {
    private static final Logger _log = LoggerFactory.getLogger(ApplicationMetrics.class);
    private static final String UNDERSCORE = "_";
    private static final String JOB_LABEL_MEAL = "meal";
    private static final String JOB_LABEL_JOBNAME = "jobname";
    private static Map<String, Gauge> _meters = new HashMap<>();
    private static String _jobStatusMeterKey = null;
    private static String _jobDurationMeterKey = null;
    private static String _jobType = null;

    private static final ThreadLocal<Instant> _lastSampledInstant = new ThreadLocal<>();

    private static void initializeKeysAndLabels(CurrentJobInfo currentJobInfo) {
        _jobStatusMeterKey = String.join(UNDERSCORE, currentJobInfo.feedName(), JobMeters.JOB_STATUS.getName());
        _jobDurationMeterKey = String.join(UNDERSCORE, currentJobInfo.feedName(), JobMeters.JOB_DURATION.getName());
        _jobType = currentJobInfo.jobType();
    }

    public static void metersCreate(CurrentJobInfo currentJobInfo) {
        _lastSampledInstant.set(Instant.now());
        initializeKeysAndLabels(currentJobInfo);
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
        _log.info("Job running metrics");
        Gauge statusGauge = _meters.get(_jobStatusMeterKey);
        statusGauge.labelValues(_jobType).set(JobStatus.RUNNING.getStatus());

        Gauge durationGauge = _meters.get(_jobDurationMeterKey);
        double currentDuration = durationGauge.labelValues(_jobType).get();
        long elapsedSecondsSinceLastSample = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        _lastSampledInstant.set(Instant.now());
        durationGauge.labelValues(_jobType).set(total);

    }

    public static void jobCompleted() {
        _log.info("Job completed metrics");
        _meters.get(_jobStatusMeterKey).labelValues(_jobType).set(JobStatus.COMPLETED.getStatus());

        Gauge durationGauge = _meters.get(_jobDurationMeterKey);
        double currentDuration = durationGauge.labelValues(_jobType).get();
        long elapsedSecondsSinceLastSample = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        _lastSampledInstant.set(Instant.now());
        durationGauge.labelValues(_jobType).set(total);
    }

    public static void jobFailed() {
        _log.info("Job failed metrics");
        _meters.get(_jobStatusMeterKey).labelValues(_jobType).set(JobStatus.FAILED.getStatus());

        Gauge durationGauge = _meters.get(_jobDurationMeterKey);
        double currentDuration = durationGauge.labelValues(_jobType).get();
        long elapsedSecondsSinceLastSample = Duration.between(_lastSampledInstant.get(), Instant.now()).toSeconds();
        long total = elapsedSecondsSinceLastSample + (long) currentDuration;
        _lastSampledInstant.set(Instant.now());
        durationGauge.labelValues(_jobType).set(total);
    }

    public static void metersReset() {
        _meters.get(_jobDurationMeterKey).labelValues(_jobType).set(0);
    }

}
