package dev.rohitahuja.batchjobs;

import java.util.concurrent.ThreadLocalRandom;

public interface BatchJob {
    void run();

    default long getRandomExecutionTime() {
        return ThreadLocalRandom.current().nextLong(4000, 14001);
    }

    default void letsSleep() {
        try {
            Thread.sleep(getRandomExecutionTime());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
