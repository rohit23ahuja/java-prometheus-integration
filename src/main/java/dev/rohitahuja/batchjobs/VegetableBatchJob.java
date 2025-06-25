package dev.rohitahuja.batchjobs;

import dev.rohitahuja.metrics.MetricsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class VegetableBatchJob implements BatchJob {
    private static final Logger _log = LoggerFactory.getLogger(VegetableBatchJob.class);

    @Override
    public void run() {
        _log.info("Running Vegetable Batch Job");

        List<String> vegetables = readItems();
        List<String> processed = processItems(vegetables);
        writeItems(processed);

        _log.info("Vegetable Batch Job completed.");
    }

    private List<String> readItems() {
        return List.of("carrot", "spinach", "broccoli");
    }

    private List<String> processItems(List<String> items) {
        return items.stream().map(s -> s + " (fresh)").toList();
    }

    private void writeItems(List<String> items) {
        items.forEach(item -> _log.info("Processed vegetable: {}", item));
        MetricsManager.itemsProcessedCount.set(items.size());
    }
}

