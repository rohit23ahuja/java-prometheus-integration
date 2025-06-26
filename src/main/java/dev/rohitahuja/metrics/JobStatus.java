package dev.rohitahuja.metrics;

public enum JobStatus {
    RUNNING(1),
    COMPLETED(2),
    FAILED(3);

    private final int status;

    JobStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
