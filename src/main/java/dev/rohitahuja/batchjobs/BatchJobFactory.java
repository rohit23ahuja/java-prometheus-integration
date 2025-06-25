package dev.rohitahuja.batchjobs;

import java.util.Map;

public class BatchJobFactory {
    private static final Map<String, BatchJob> _jobs = Map.of(
            "fruit", new FruitBatchJob(),
            "vegetable", new VegetableBatchJob()
    );

    public static BatchJob getJob(String jobName) {
        return _jobs.get(jobName);
    }
}
