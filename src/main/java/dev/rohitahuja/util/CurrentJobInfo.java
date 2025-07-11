package dev.rohitahuja.util;

public record CurrentJobInfo(String jobName, String feedName, String jobType) {

    public static CurrentJobInfo create(String jobName) {
        String[] parts = jobName.split("_", 2);
        String feedName = parts.length > 0 ? parts[0] : "";
        String jobType = parts.length > 1 ? parts[1] : "";
        return new CurrentJobInfo(jobName, feedName, jobType);
    }
}




