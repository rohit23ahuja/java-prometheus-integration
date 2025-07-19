package dev.rohitahuja.metrics;

public enum JobStatus {
    RUNNING(1,"running"),
    COMPLETED(2, "completed"),
    FAILED(3, "failed");

    private final int status;
    private final String label;

    JobStatus(int status, String label) {
        this.status = status;
        this.label = label;
    }

    public int getStatus() {
        return status;
    }

    public String getLabel() { return label;}
}
