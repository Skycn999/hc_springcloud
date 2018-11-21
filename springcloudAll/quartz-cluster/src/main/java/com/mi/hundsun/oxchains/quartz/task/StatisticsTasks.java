/*
 * Copyright (c) 2015-2018, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.quartz.task;

import com.mi.hundsun.oxchains.quartz.task.handler.CountServiceHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 枫亭
 * @date 2018-06-11 10:16.
 */
@Slf4j
public class StatisticsTasks {

//    @Autowired
//    private CountServiceHandler countServiceHandler;
//
//    /**
//     * 统计收益汇总中的部分信息-如交易额和交易手续费
//     */
//    @Scheduled(cron = "${cronCountTransServiceInfo}")
//    public void countTransServiceInfo(){
//        countServiceHandler.countTransServiceInfo();
//    }
//
//    /**
//     * 统计交易情况
//     */
//    @Scheduled(cron = "${cronCountTransInfo}")
//    public void countTransInfo(){
//        countServiceHandler.countTransInfo();
//    }
//
//    /**
//     * 统计用户注册情况
//     */
//    @Scheduled(cron = "${cronCountTransInfo}")
//    public void countUserRegisterInfo() {
//
//    }

    /**
     * 统计收益汇总中的部分信息-如交易额和交易手续费
     */
    @Scheduled(cron = "${cronCountTransServiceInfo}")
    public void countTransServiceInfo(){
//        countTransactionService.countTransServiceInfo();
    }

    /**
     * 统计交易情况
     */
    @Scheduled(cron = "${cronCountTransInfo}")
    public void countTransInfo(){
//        countTransactionService.countTransInfo();
    }

    /**
     * 统计用户注册情况
     */
    @Scheduled(cron = "${cronCountTransInfo}")
    public void countUserRegisterInfo() {

    }

}
