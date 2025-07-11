* prometheus push gateway functions behavior - push and push_add
* push is like replacing a whole page in a notebook. You're starting fresh for that specific job and instance. whichever 
batch job is triggered last, only that metrics will remain. 
* push_add is like adding or editing notes on a page in a notebook. You're either adding new notes or updating existing 
ones within the same context (job and instance). metrics of all jobs which will be present

curl -X POST -g 'http://localhost:9090/api/v1/admin/tsdb/delete_series?match[]={job="java-prometheus-integration"}'

curl -X POST http://localhost:9090/api/v1/admin/tsdb/clean_tombstones

prometheus.exe --web.enable-admin-api

curl -X DELETE http://localhost:9091/metrics/job/java-prometheus-integration

https://www.metricfire.com/blog/prometheus-pushgateways-everything-you-need-to-know/
https://prometheus.io/docs/practices/pushing/
https://prometheus.io/docs/practices/instrumentation/#batch-jobs
https://last9.io/blog/prometheus-pushgateway/

https://stackoverflow.com/questions/62875905/monitor-the-status-of-batch-jobs-using-metrics-in-prometheus

https://prometheus.io/docs/prometheus/latest/querying/functions/#time
https://www.nicktriller.com/blog/monitoring-batch-jobs-with-prometheus/
https://stackoverflow.com/questions/60651605/are-counters-thread-safe-in-micrometer
https://stackoverflow.com/questions/68838448/will-updating-a-micrometer-gauge-ever-block-the-calling-thread

https://www.google.com/search?q=prometheus+counter+for+task+running+duration&sca_esv=cca5566734bdcf74&ei=Td5baN_mKPmj1e8P7OfjiAw&oq=prometheus+counter+for+task+running+durat&gs_lp=Egxnd3Mtd2l6LXNlcnAiKXByb21ldGhldXMgY291bnRlciBmb3IgdGFzayBydW5uaW5nIGR1cmF0KgIIADIFECEYoAEyBRAhGKABMgUQIRigAUiG1gVQpwhYtcYFcAF4AJABAJgB_QKgAdREqgEEMy0yNbgBA8gBAPgBAZgCGKAC3UDCAgoQABiwAxjWBBhHwgIFEAAYgATCAgsQABiABBiRAhiKBcICBhAAGBYYHsICCxAAGIAEGIYDGIoFwgIIEAAYgAQYogTCAggQABgIGA0YHsICCBAAGKIEGIkFwgIFEAAY7wXCAgUQIRifBcICBxAhGKABGAqYAwCIBgGQBgiSBwYxLjMtMjOgB4SPAbIHBDMtMjO4B8ZAwgcGMi0xNy43yAeXAQ&sclient=gws-wiz-serp&safe=active&ssui=on
