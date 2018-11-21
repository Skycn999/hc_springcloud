package com.jbc.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class BaseQuartzJob implements Job {
    private static Logger log = LoggerFactory.getLogger(BaseQuartzJob.class);
    private static ConcurrentMap<String, JobRunStatus> jobsStatus = new ConcurrentHashMap<>();

    public static List<JobRunStatus> getJobsStatus() {
        return new ArrayList<>(jobsStatus.values());
    }

    public final void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDetail jobDetail = context.getJobDetail();
            String jobName = jobDetail.getKey().getName();
            JobRunStatus runStatus = new JobRunStatus(jobName);
            if (jobsStatus.containsKey(jobName)) {
                log.warn("定时任务已运行，请核实是否可以同时2个实例", jobName);
            }
            jobsStatus.put(jobName, runStatus);
            String info = String.format("定时任务%s开始执行！", jobName);
            log.info(info);
            executeInternal(context);
            info = String.format("定时任务%s执行结束！", jobName);
            log.info(info);
            jobsStatus.remove(jobName);
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }

    }

    protected abstract void executeInternal(JobExecutionContext context) throws JobExecutionException;
}
