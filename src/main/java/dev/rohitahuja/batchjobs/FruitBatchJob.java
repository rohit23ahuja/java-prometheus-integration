package dev.rohitahuja.batchjobs;

import dev.rohitahuja.metrics.MetricsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FruitBatchJob implements BatchJob {
    private static final Logger _log = LoggerFactory.getLogger(FruitBatchJob.class);

    @Override
    public void run() {
        _log.info("Running Fruit Batch Job");

        List<String> fruits = readItems();
        List<String> processedFruits = processItems(fruits);
        writeItems(processedFruits);

        _log.info("Fruit Batch Job completed.");
    }

    private List<String> readItems() {
        return List.of("apple", "banana", "orange");
    }

    private List<String> processItems(List<String> items) {
        return items.stream().map(String::toUpperCase).toList();
    }

    private void writeItems(List<String> items) {
        items.forEach(item -> _log.info("Processed fruit: {}", item));
        MetricsManager.itemsProcessedCount.set(items.size());
    }
}
