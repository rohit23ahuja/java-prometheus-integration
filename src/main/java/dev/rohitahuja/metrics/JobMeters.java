package dev.rohitahuja.metrics;

public enum JobMeters {
    JOB_STATUS("job_status"),
    JOB_DURATION("job_duration");

    private final String name;

    JobMeters(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
