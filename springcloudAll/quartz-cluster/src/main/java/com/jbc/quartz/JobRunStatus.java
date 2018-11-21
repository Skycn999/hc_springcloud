package com.jbc.quartz;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务运行状态调试
 * @author hukw18431
 *
 */
public class JobRunStatus {
    private String jobName  = "";
    /**
     * 任务运行时间
     */
    private Date   jobStart = new Date();
    /**
     * 任务当前步骤描述
     */
    private String stepDes  = "";
    /**
     * 备注
     */
    private String remark   = "";

    public JobRunStatus(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobStart() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(jobStart);
    }

    public String getStepDes() {
        return stepDes;
    }

    public void setStepDes(String stepDes) {
        this.stepDes = stepDes;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
