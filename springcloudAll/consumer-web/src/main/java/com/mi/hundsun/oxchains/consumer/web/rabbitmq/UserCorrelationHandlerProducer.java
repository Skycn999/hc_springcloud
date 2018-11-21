/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.rabbitmq;

import com.mi.hundsun.oxchains.base.core.po.user.Users;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 用户相关处理队列 - 生产者
 * @author 枫亭
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class UserCorrelationHandlerProducer {

    @Value("${spring.rabbitmq.userQueue:userQueue}")
    private String userQueue;

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendUserDistributeTask(Users user) {
        try {
            amqpTemplate.convertAndSend(userQueue, user);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }


}

