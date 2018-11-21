/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关处理队列 - 生产者
 * @author 枫亭
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class TxHandlerProducer {

    @Value("${spring.rabbitmq.txSellOutQueue:txSellOutQueue}")
    private String txSellOutQueue;
    @Value("${spring.rabbitmq.txBuyInQueue:txBuyInQueue}")
    private String txBuyInQueue;


    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendBuyInTxTask(String exchangeNo, String currencyPair, MainDelegation delegation) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("exchangeNo", exchangeNo);
            params.put("currencyPair", currencyPair);
            params.put("delegation", JSON.toJSONString(delegation));
            amqpTemplate.convertAndSend(txBuyInQueue, params);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }

    public void sendSellOutTxTask(String exchangeNo, String currencyPair, MainDelegation delegation) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("exchangeNo", exchangeNo);
            params.put("currencyPair", currencyPair);
            params.put("delegation", JSON.toJSONString(delegation));
            amqpTemplate.convertAndSend(txSellOutQueue, params);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }


}

