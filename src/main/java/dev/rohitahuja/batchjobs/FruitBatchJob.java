package dev.rohitahuja.batchjobs;

import dev.rohitahuja.metrics.ApplicationMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FruitBatchJob implements BatchJob {
    private static final Logger _log = LoggerFactory.getLogger(FruitBatchJob.class);

    @Override
    public void run() {
        _log.info("Running Fruit Batch Job");

        List<String> fruits = readItems();
        ApplicationMetrics.jobRunning();
        List<String> processedFruits = processItems(fruits);
        ApplicationMetrics.jobRunning();
        writeItems(processedFruits);
        ApplicationMetrics.jobRunning();

        _log.info("Fruit Batch Job completed.");
    }

    private List<String> readItems() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return List.of("apple", "banana", "orange");
    }

    private List<String> processItems(List<String> items) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return items.stream().map(String::toUpperCase).toList();
    }

    private void writeItems(List<String> items) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        items.forEach(item -> _log.info("Processed fruit: {}", item));
    }
}
