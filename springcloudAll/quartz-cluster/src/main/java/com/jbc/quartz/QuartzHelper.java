package com.jbc.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzHelper {
    private static final String JOB_GROUP_NAME = "defaultgroup";
    private static Logger       logger         = LoggerFactory.getLogger(QuartzHelper.class);

    private QuartzHelper() {

    }

    /**
     * 不存在添加，存在者修改
     * 
     * @param jobClass
     * @param cronExpression
     * @param apply
     * @throws Exception
     */
    public static void addOrchange(Class<? extends Job> jobClass, String cronExpression,
                                   boolean apply, Scheduler sched) throws Exception {
        if (checkExists(jobClass, sched)) {
            QuartzHelper.jobreschedule(jobClass, cronExpression, sched);
        } else {
            QuartzHelper.addJob(jobClass, cronExpression, sched);
        }
        if (apply) {
            QuartzHelper.jobresume(jobClass, sched);
        } else {
            QuartzHelper.jobPause(jobClass, sched);
        }
    }

    /**
     * 判断是否存在改定时任务
     * 
     * @param jobClass
     * @return
     */
    public static boolean checkExists(Class<? extends Job> jobClass,
                                      Scheduler sched) throws Exception {
        return sched.checkExists(JobKey.jobKey(jobClass.getName(), JOB_GROUP_NAME));
    }

    /**
     * 添加一个定时任务
     * 
     * @param jobClass
     * @param cronExpression
     * @throws Exception
     */
    public static void addJob(Class<? extends Job> jobClass, String cronExpression,
                              Scheduler sched) throws Exception {

        // 构建job信息
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
            .withIdentity(jobClass.getName(), JOB_GROUP_NAME).build();

        // 表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

        // 按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(jobClass.getName(), JOB_GROUP_NAME).withSchedule(scheduleBuilder).build();

        try {
            sched.scheduleJob(jobDetail, trigger);

        } catch (SchedulerException e) {
            logger.error("创建定时任务失败", e);
            throw new RuntimeException("创建定时任务失败", e);
        }
    }

    /**
     * 更新定时任务时间
     * 
     * @param jobClass
     * @param cronExpression
     * @throws Exception
     */
    public static void jobreschedule(Class<? extends Job> jobClass, String cronExpression,
                                     Scheduler sched) throws Exception {
        try {

            TriggerKey triggerKey = TriggerKey.triggerKey(jobClass.getName(), JOB_GROUP_NAME);
            // 表达式调度构建器
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);

            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (!trigger.getCronExpression().equals(cronExpression)) {
                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(scheduleBuilder).build();
                // 按新的trigger重新设置job执行
                sched.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            logger.error("更新定时任务失败", e);
            throw new RuntimeException("更新定时任务失败", e);
        }
    }

    /**
     * 暂停一个定时任务
     * 
     * @param jobClass
     * @throws Exception
     */
    public static void jobPause(Class<? extends Job> jobClass, Scheduler sched) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例
        sched.pauseJob(JobKey.jobKey(jobClass.getName(), JOB_GROUP_NAME));
    }

    /**
     * 恢复一个定时任务
     * 
     * @param jobClass
     * @throws Exception
     */
    public static void jobresume(Class<? extends Job> jobClass, Scheduler sched) throws Exception {
        // 通过SchedulerFactory获取一个调度器实例
        sched.resumeJob(JobKey.jobKey(jobClass.getName(), JOB_GROUP_NAME));
    }

    /**
     * 删除任务
     * 
     * @param jobClass
     * @throws Exception
     */
    public static void jobdelete(Class<? extends Job> jobClass, Scheduler sched) throws Exception {
        sched.pauseTrigger(TriggerKey.triggerKey(jobClass.getName(), JOB_GROUP_NAME));
        sched.unscheduleJob(TriggerKey.triggerKey(jobClass.getName(), JOB_GROUP_NAME));
        sched.deleteJob(JobKey.jobKey(jobClass.getName(), JOB_GROUP_NAME));
    }

}
