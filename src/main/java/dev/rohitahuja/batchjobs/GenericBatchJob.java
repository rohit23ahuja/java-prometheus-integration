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
    public void run() {
        _log.info("Running {} batch job", jobName);
        letsSleep();
        doSomething();
        letsSleep();
        stillDoingSomething();
        letsSleep();
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
