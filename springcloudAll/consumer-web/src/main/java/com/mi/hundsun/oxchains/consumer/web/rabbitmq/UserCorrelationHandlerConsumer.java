/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.rabbitmq;

import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.consumer.web.service.user.UserInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 枫亭
 * @description 用户相关处理队列 - 消费者
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class UserCorrelationHandlerConsumer {

    @Autowired
    private UserInterface userInterface;

    @RabbitListener(queues = "#{waitSendUserQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleUserDistributeAddress(Users user) {
        if (null == user) {
            return;
        }
        try {
            //user
            userInterface.distributeAddressToUser(user.getId());
        } catch (Exception e) {
            log.error("接收用户分配充币地址MQ时出现异常:{}", e);
            throw new RuntimeException(e);
        }
    }
}

