package dev.rohitahuja;
import dev.rohitahuja.batchjobs.BatchJob;
import dev.rohitahuja.batchjobs.BatchJobFactory;
import dev.rohitahuja.metrics.ApplicationMetrics;
import dev.rohitahuja.metrics.PushMetrics;
import dev.rohitahuja.util.CurrentJobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaPrometheusIntegration {
    private static final Logger _log = LoggerFactory.getLogger(JavaPrometheusIntegration.class);

    public static void main(String[] args) {
        String jobName = args[0];
        CurrentJobInfo.create(jobName);
        try {
            PushMetrics.start();
            ApplicationMetrics.metersCreate("fruit");
            if (args.length == 0) {
                _log.error("No job name provided. Please provide job name as argument");
                return;
            }
            _log.info("Starting batch job: {}", jobName);
            BatchJob job = BatchJobFactory.getJob(jobName);
            if (job == null) {
                _log.error("No batch job found with name '{}'.", jobName);
                return;
            }
            ApplicationMetrics.jobRunning();
            job.run();
            _log.info("Batch job '{}' completed successfully.", jobName);
            ApplicationMetrics.jobCompleted();
        } catch (Exception e) {
            _log.error("Error while executing batch job '{}': {}", jobName, e.getMessage(), e);
            ApplicationMetrics.jobFailed();
        } finally {
            ApplicationMetrics.metersReset(jobName);
            PushMetrics.stop();
        }
    }


}
