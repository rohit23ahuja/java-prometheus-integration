package dev.rohitahuja.util;


import java.util.Map;

public final class CurrentJobInfo {
    private static Map<JobKeys, String> _map;

    public static void create(String jobName) {
        String[] parts = jobName.split("_", 2);
        final String feedName = parts.length > 0 ? parts[0] : "";
        final String jobType = parts.length > 1 ? parts[1] : "";
        _map = Map.of(
                JobKeys.JOBNAME, jobName,
                JobKeys.FEEDNAME, feedName,
                JobKeys.JOBTYPE, jobType);
    }

    public static String jobName() {
        return _map.get(JobKeys.JOBNAME);
    }

    public static String feedName() {
       return _map.get(JobKeys.FEEDNAME);
    }

    public static String jobType() {
        return _map.get(JobKeys.JOBTYPE);
    }

}

enum JobKeys {
    JOBNAME,
    FEEDNAME,
    JOBTYPE;
}
