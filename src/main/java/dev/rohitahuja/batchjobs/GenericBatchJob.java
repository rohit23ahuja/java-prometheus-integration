package dev.rohitahuja.batchjobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericBatchJob implements BatchJob {

    private static final Logger _log = LoggerFactory.getLogger(GenericBatchJob.class);
    private final String jobName;

    public GenericBatchJob(String jobName) {
        this.jobName = jobName;
    }

    @Override
    public void run(long sleepTime) {
        _log.info("Running {} batch job", jobName);
        letsSleep(sleepTime);
        doSomething();
        letsSleep(sleepTime);
        stillDoingSomething();
        letsSleep(sleepTime);
        willKeepOnDoingSomething();
    }

    public void doSomething() {
        _log.info(" {} : Doing something", jobName);
    }

    public void stillDoingSomething() {
        _log.info(" {} : Still doing something", jobName);
    }

    public void willKeepOnDoingSomething() {
        _log.info(" {} : Will keep on doing something", jobName);
    }

}
