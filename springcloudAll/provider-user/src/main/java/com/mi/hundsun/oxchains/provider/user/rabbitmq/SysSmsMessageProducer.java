/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.user.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 枫亭
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class SysSmsMessageProducer {

    @Value("${spring.rabbitmq.smsQueue:smsQueue}")
    private String smsQueue;
    @Value("${spring.rabbitmq.emailQueue:emailQueue}")
    private String emailQueue;
    @Value("${spring.rabbitmq.insideQueue:insideQueue}")
    private String insideQueue;
    @Value("${spring.rabbitmq.insideQueue:inside2Queue}")
    private String inside2Queue;


    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendSms( String mobile,String type) {
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("mobile",mobile);
        amqpTemplate.convertAndSend(smsQueue , map);
    }

    public void sendLetter(Integer userId, String type) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("type",type);
        amqpTemplate.convertAndSend(insideQueue , map);
    }

    public void sendLetter2(Integer userId,String currency,String num, String type) {
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("currency",currency);
        map.put("num",num);
        map.put("type",type);
        amqpTemplate.convertAndSend(inside2Queue , map);
    }

    public void sendEmail(String email,String type) {
        Map<String,String> map = new HashMap<>();
        map.put("type",type);
        map.put("email",email);
        amqpTemplate.convertAndSend(emailQueue , map);
    }
}

