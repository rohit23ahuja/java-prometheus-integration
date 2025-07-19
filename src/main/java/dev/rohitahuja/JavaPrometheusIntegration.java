package dev.rohitahuja;

import dev.rohitahuja.batchjobs.BatchJob;
import dev.rohitahuja.batchjobs.GenericBatchJobFactory;
import dev.rohitahuja.metrics.BatchJobMetrics;
import dev.rohitahuja.metrics.BatchJobMetricsSingleApproach;
import dev.rohitahuja.metrics.PushMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.random.RandomGenerator;

public class JavaPrometheusIntegration {
    private static final Logger _log = LoggerFactory.getLogger(JavaPrometheusIntegration.class);

    public static void main(String[] args) {
        if (args.length == 0) {
            _log.error("No job name provided. Please provide job name as argument");
            return;
        }
        String jobName = args[0];
        BatchJobMetrics batchJobMetrics =  new BatchJobMetrics(jobName);
        PushMetrics.start(jobName);
        try {
            _log.info("Starting batch job: {}", jobName);

            batchJobMetrics.jobRunning();
            BatchJob job = GenericBatchJobFactory.getJob(jobName);

            long l = 0;
            switch (jobName) {
                case "i20_petrol":
                    l = 1000;
                    break;
                case "a6_diesel":
                    l = 2000;
                    break;
                case "baleno_petrol":
                    l = 3000;
                    break;
                default:
                    l =RandomGenerator.getDefault().nextLong(4000, 20000);
            }

            job.run(l);
            switch (jobName) {
                case "eeco_cng":
                case "verito_diesel":
                case "dzire_cng":
                    throw new Exception(jobName + " job failed.");
//                case "landcruiser_hybrid":
//                case "camry_hybrid":
//                case "vitara_hybrid":
//                    break;
                default:
                    _log.info("Batch job '{}' completed successfully.", jobName);

                    batchJobMetrics.jobCompleted();
            }
            //_log.info("Batch job '{}' completed successfully.", jobName);
            //batchJobMetrics.jobCompleted();
        } catch (Exception e) {
            _log.error("Error while executing batch job '{}': {}", jobName, e.getMessage(), e);
            batchJobMetrics.jobFailed();
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
