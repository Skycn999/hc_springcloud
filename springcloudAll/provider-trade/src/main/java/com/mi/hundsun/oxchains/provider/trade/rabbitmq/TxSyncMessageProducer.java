/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.rabbitmq;

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
 * @author 枫亭
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class TxSyncMessageProducer {

    @Value("${spring.rabbitmq.subDelegateQueue:subDelegateQueue}")
    private String txSubDelegationQueue;
    @Value("${spring.rabbitmq.subDelegateFailureQueue:sub-delegate-failure-queue}")
    private String subDelegateFailureQueue;
    @Value("${spring.rabbitmq.subDelegateSuccessQueue:sub-delegate-success-queue}")
    private String subDelegateSuccessQueue;
    @Value("${spring.rabbitmq.revokeDelegateFailureQueue:revoke-delegate-failure-queue}")
    private String revokeDelegateFailureQueue;
    @Value("${spring.rabbitmq.mainDelegateFailureQueue:main-delegate-failure-queue}")
    private String mainDelegateFailureQueue;

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendSubMsg(SubDelegation sub) {
        try {

            amqpTemplate.convertAndSend(txSubDelegationQueue, sub);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }


    public void sendSubDelegateFailureTask(SubDelegation sub, String msg) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg", msg);
            params.put("subDelegation", JSON.toJSONString(sub));
            amqpTemplate.convertAndSend(subDelegateFailureQueue, params);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }

    public void sendRevokeDelegateFailureTask(SubDelegation sub, String msg) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("msg", msg);
            params.put("subDelegation", JSON.toJSONString(sub));
            amqpTemplate.convertAndSend(revokeDelegateFailureQueue, params);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }

    public void sendSubDelegateSuccessTask(SubDelegation sub, OrderQryRes result, String entrustNo) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("entrustNo", entrustNo);
            params.put("result", JSON.toJSONString(result));
            params.put("subDelegation", JSON.toJSONString(sub));
            amqpTemplate.convertAndSend(subDelegateSuccessQueue, params);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }

    public void sendMainDelegateFailureTask(MainDelegation delegate) {
        try {
            amqpTemplate.convertAndSend(mainDelegateFailureQueue, delegate);
        } catch (Exception e) {
            log.error("发送失败:{}", e.getMessage());
        }
    }
}

