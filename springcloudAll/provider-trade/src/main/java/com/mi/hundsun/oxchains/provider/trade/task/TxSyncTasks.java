/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.task;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.service.tx.MainDelegationService;
import com.mi.hundsun.oxchains.base.core.service.tx.SubDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.provider.trade.rabbitmq.TxSyncMessageProducer;
import com.mi.hundsun.oxchains.provider.trade.service.ExchangeInterface;
import com.mi.hundsun.oxchains.provider.trade.service.TxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 枫亭
 * @description 交易相关同步服务
 * @date 2018-05-23 22:55.
 */
@Slf4j
@Component
public class TxSyncTasks {

    @Autowired
    private TxService txService;
    @Autowired
    private SubDelegationService subDelegationService;
    @Autowired
    private ExchangeInterface exchangeInterface;

    @Autowired
    private TxSyncMessageProducer txSyncMessageProducer;

    @Autowired
    private MainDelegationService mainDelegationService;
    /**
     * 一秒钟执行一次 同步委托中的子委托
     */
    @Scheduled(cron = "${txSyncSubDelegateCron}")
    public void syncSubDelegationOfBuyIn() {
        List<SubDelegation> tradingSubDelegateList = subDelegationService.getTradingSubDelegates();
        for (SubDelegation sub : tradingSubDelegateList) {
//            txSyncMessageProducer.sendSubMsg(sub);
            ResultEntity motherAccountByEac = exchangeInterface.findMotherAccountByExNoAndAccountName(sub.getExchange(), sub.getMotherAccount());
            if (motherAccountByEac.getCode() == ResultEntity.SUCCESS) {
                MotherAccountInfoModel account = JSON.parseObject(motherAccountByEac.getData().toString(), MotherAccountInfoModel.class);
                txService.singleQryOrder(account, sub.getCoinCode() + "_" + sub.getCoinCurrency(), sub.getBillNo());
            } else {
                log.error("Query Exchange's Account was Failed,The Reason Is:{}", motherAccountByEac.getMessage());
            }
        }
    }

    /**
     * 10秒钟执行一次 同步委托中的主委托
     */
    @Scheduled(cron = "${txSyncMainDelegateCron}")
    public void syncMainDelegation() {
        List<MainDelegation> mainDelegations = mainDelegationService.findTradingMainDelegateList();
        for (MainDelegation main : mainDelegations) {
            List<SubDelegation> subDelegations = subDelegationService.getSubDelegatesByMainDelegateNo(main.getDelegateNo(), main.getUserId());
            //没有拆单
            if (subDelegations.size() == 1) {
                SubDelegation sub = subDelegations.get(0);
                if (SubDelegation.STATE.FAILED.code == sub.getState()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.FAILED.code, "子委托下单失败,失败原因:" + sub.getInfo(), main.getDelegateNo());
                } else if (SubDelegation.STATE.TRADING.code == sub.getState() || SubDelegation.STATE.REPORTED.code == sub.getState()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.COMMISSIONED_IN.code, sub.getStateFormatter(), main.getDelegateNo());
                } else if (SubDelegation.STATE.PART_OF_DEAL.code == sub.getState()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.COMMISSIONED_IN.code, sub.getStateFormatter(), main.getDelegateNo());
                } else if (SubDelegation.STATE.REVOKING.code == sub.getState()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.COMMISSIONED_IN.code, sub.getStateFormatter(), main.getDelegateNo());
                } else {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.FINISHED.code, sub.getStateFormatter(), main.getDelegateNo());
                }
            } else if (subDelegations.size() > 1) {
                //已经拆单
                int successCount = 0;
                int revokeCount = 0;
                int failedCount = 0;
                for (SubDelegation sub : subDelegations) {
                    if (sub.getState() == SubDelegation.STATE.DEAL.code) {
                        successCount++;
                        continue;
                    }
                    if (sub.getState() == SubDelegation.STATE.REVOKED.code) {
                        revokeCount++;
                        continue;
                    }
                    if (sub.getState() == SubDelegation.STATE.FAILED.code) {
                        failedCount++;
                    }
                }
                if (successCount == subDelegations.size()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.FINISHED.code, "子委托全部成交-m", main.getDelegateNo());
                    return;
                }
                if (revokeCount == subDelegations.size()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.FINISHED.code, "子委托全部撤回-m", main.getDelegateNo());
                    return;
                }
                if (revokeCount < subDelegations.size()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.FINISHED.code, "子委托部分撤回-m", main.getDelegateNo());
                    return;
                }
                if (failedCount == subDelegations.size()) {
                    mainDelegationService.updateStateByMainNo(MainDelegation.STATE.FINISHED.code, "子委托全部失败-m", main.getDelegateNo());
                }
            }

        }
    }
}
