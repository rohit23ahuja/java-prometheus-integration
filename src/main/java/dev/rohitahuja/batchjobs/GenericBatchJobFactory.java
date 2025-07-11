package dev.rohitahuja.batchjobs;

public class GenericBatchJobFactory {

    public static BatchJob getJob(String jobName) {
        return new GenericBatchJob(jobName);
    }
}
