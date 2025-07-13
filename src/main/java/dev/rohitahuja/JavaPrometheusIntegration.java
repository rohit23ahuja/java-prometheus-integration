package dev.rohitahuja;

import dev.rohitahuja.batchjobs.BatchJob;
import dev.rohitahuja.batchjobs.GenericBatchJobFactory;
import dev.rohitahuja.metrics.ApplicationMetricsGenericBatchJob;
import dev.rohitahuja.metrics.PushMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaPrometheusIntegration {
    private static final Logger _log = LoggerFactory.getLogger(JavaPrometheusIntegration.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            _log.error("No job name provided. Please provide job name as argument");
            return;
        }
        String jobName = args[0];
        try {
            PushMetrics.start();
            ApplicationMetricsGenericBatchJob.metersCreate(jobName);

            BatchJob job = GenericBatchJobFactory.getJob(jobName);
            ApplicationMetricsGenericBatchJob.jobRunning();
            job.run();
            switch (jobName.split("_", 2)[1]) {
                case "cng":
                    throw new Exception("CNG batch job failed.");
                case "hybrid":
                    break;
                default:
                    _log.info("Batch job '{}' completed successfully.", jobName);
                    ApplicationMetricsGenericBatchJob.jobCompleted();
            }
            //_log.info("Batch job '{}' completed successfully.", jobName);
            //ApplicationMetricsGenericBatchJob.jobCompleted();
        } catch (Exception e) {
            _log.error("Error while executing batch job '{}': {}", jobName, e.getMessage(), e);
            ApplicationMetricsGenericBatchJob.jobFailed();
        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            PushMetrics.stop();
        }
    }


//    public static void main(String[] args) {
//        if (args.length == 0) {
//            _log.error("No job name provided. Please provide job name as argument");
//            return;
//        }
//        CurrentJobInfo currentJobInfo = CurrentJobInfo.create(args[0]);
//        try {
//            PushMetrics.start();
//            ApplicationMetrics.metersCreate(currentJobInfo);
//            _log.info("Starting batch job: {}", currentJobInfo.feedName());
//            BatchJob job = BatchJobFactory.getJob(currentJobInfo.feedName());
//            if (job == null) {
//                _log.error("No batch job found with name '{}'.", currentJobInfo.feedName());
//                return;
//            }
//            ApplicationMetrics.jobRunning();
//            job.run();
//            _log.info("Batch job '{}' completed successfully.", currentJobInfo.feedName());
//            ApplicationMetrics.jobCompleted();
//        } catch (Exception e) {
//            _log.error("Error while executing batch job '{}': {}", currentJobInfo.feedName(), e.getMessage(), e);
//            ApplicationMetrics.jobFailed();
//        } finally {
//            try {
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            ApplicationMetrics.metersReset();
//            PushMetrics.stop();
//        }
//    }


}
