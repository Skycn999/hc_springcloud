/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.quote.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author 枫亭
 * @description 用户相关处理队列 - 消费者
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class QuoteConsumer {


//    @RabbitListener(queues = "#{waitSendBianDepthQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void receiveDepthOutputMsg(String msg) {
        log.info("Received :" + msg);
    }
}

