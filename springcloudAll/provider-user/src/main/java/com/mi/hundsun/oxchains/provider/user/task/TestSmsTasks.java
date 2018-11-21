///*
// * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
// */
//package com.mi.hundsun.oxchains.provider.user.task;
//
//import com.mi.hundsun.oxchains.provider.user.rabbitmq.SysSmsMessageProducer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
///**
// * @author 枫亭
// * @description 交易相关同步服务
// * @date 2018-05-23 22:55.
// */
//@Slf4j
//@Component
//public class TestSmsTasks {
//
//    @Autowired
//    private SysSmsMessageProducer sysSmsMessageProducer;
//
//    @Scheduled(cron = "0/2 * * * * *")
//    public void testSmsRabbit() {
//            sysSmsMessageProducer.sendSms("你好啊,我是发送短信的." + new Date().getTime(), "18758871684");
//    }
//
//    @Scheduled(cron = "0/3 * * * * *")
//    public void testEmailRabbit() {
//        sysSmsMessageProducer.sendEmail("邮件标题","你好啊,我是发送邮件的。" + new Date().getTime(), "18758871684");
//    }
//
//    @Scheduled(cron = "0/4 * * * * *")
//    public void testInsideRabbit() {
//        sysSmsMessageProducer.sendLetter(1,"你好啊,我的发送站内信的。" + new Date().getTime(), "18758871684");
//    }
//}
