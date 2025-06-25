package dev.rohitahuja;


import dev.rohitahuja.batchjobs.BatchJob;
import dev.rohitahuja.batchjobs.BatchJobFactory;
import dev.rohitahuja.batchjobs.FruitBatchJob;
import dev.rohitahuja.batchjobs.VegetableBatchJob;
import dev.rohitahuja.metrics.MetricsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class JavaPrometheusIntegration {
    private static final Logger _log = LoggerFactory.getLogger(JavaPrometheusIntegration.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            _log.error("No job name provided. Please provide job name as argument");
            return;
        }
        String jobName = args[0];
        _log.info("Starting batch job: {}", jobName);

        BatchJob job = BatchJobFactory.getJob(jobName);

        if (job == null) {
            _log.error("No batch job found with name '{}'.", jobName);
            return;
        }

        try {
            job.run();
            _log.info("Batch job '{}' completed successfully.", jobName);
        } catch (Exception e) {
            _log.error("Error while executing batch job '{}': {}", jobName, e.getMessage(), e);
        } finally {
            try {
                MetricsManager.pushGateway.push();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
