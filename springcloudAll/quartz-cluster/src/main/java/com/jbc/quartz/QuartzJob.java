package com.jbc.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class QuartzJob extends BaseQuartzJob {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("亲，这是测试定时任务，不用关注！");

    }

}
