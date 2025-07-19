package dev.rohitahuja.batchjobs;

import java.util.concurrent.ThreadLocalRandom;

public interface BatchJob {
    void run(long sleepTime);

    default long getRandomExecutionTime() {
        return ThreadLocalRandom.current().nextLong(4000, 14001);
    }

    default void letsSleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
