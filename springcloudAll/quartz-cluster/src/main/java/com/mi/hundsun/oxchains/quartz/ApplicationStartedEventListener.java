package com.mi.hundsun.oxchains.quartz;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mi.hundsun.oxchains.quartz.job.CountTransInfoJob;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.jbc.quartz.QuartzHelper;
import com.jbc.quartz.QuartzJob;

@WebListener
public class ApplicationStartedEventListener implements ServletContextListener {
    private static final Logger log = LoggerFactory
        .getLogger(ApplicationStartedEventListener.class);
    @Autowired
    Scheduler                   scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("开始初始化定时任务");
        try {
            // --------------------------------------------
            QuartzHelper.addOrchange(QuartzJob.class, "0 */1 * * * ?", true, scheduler);
            QuartzHelper.addOrchange(CountTransInfoJob.class, "10 * * * * ?", true, scheduler);
            if (!scheduler.isShutdown())
                scheduler.start();
        } catch (Exception e) {
            log.warn("定时任务Job使用异常:" + e.toString(), e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // TODO Auto-generated method stub

    }
}
