/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.service.tx.SubDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.provider.trade.service.ExchangeInterface;
import com.mi.hundsun.oxchains.provider.trade.service.TxOrderHandlerService;
import com.mi.hundsun.oxchains.provider.trade.service.TxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 枫亭
 * @description 同步委托订单消费者
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class TxSyncMessageConsumer {

    @Autowired
    private TxService txService;
    @Autowired
    private SubDelegationService subDelegationService;
    @Autowired
    private ExchangeInterface exchangeInterface;
    @Autowired
    private TxOrderHandlerService txOrderHandlerService;

//    @RabbitListener(queues = "#{waitSendSubDelegateQueue.name}", containerFactory = "rabbitListenerContainerFactory")
//    public void handleQrySubDelegateToMap(SubDelegation sub) {
//        if (null == sub) {
//            return;
//        }
//        log.info("接收信息,开始查询处理, 委托编号:{}" , sub.getEntrustNo());
//        try {
//            ResultEntity motherAccountByEac = exchangeInterface.findMotherAccountByExNoAndAccountName(sub.getExchange(), sub.getMotherAccount());
//            if (motherAccountByEac.getCode() == ResultEntity.SUCCESS) {
//                MotherAccountInfoModel account = JSON.parseObject(motherAccountByEac.getData().toString(), MotherAccountInfoModel.class);
//                txService.singleQryOrder(account, sub.getCoinCode() + "_" + sub.getCoinCurrency(), sub.getBillNo());
//            } else {
//                log.error("Query Exchange's Account was Failed,The Reason Is:{}", motherAccountByEac.getMessage());
//            }
//        } catch (Exception e) {
//            log.error("接收同步子委托MQ时出现异常:{}", e.getMessage());
//        }
//    }

    @RabbitListener(queues = "#{waitSendSubDelegateQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleQrySubDelegateTask(SubDelegation sub) {
        if (null == sub) {
            return;
        }
        log.info("接收信息,开始查询处理, 委托编号:{}" , sub.getEntrustNo());
        try {
            ResultEntity motherAccountByEac = exchangeInterface.findMotherAccountByExNoAndAccountName(sub.getExchange(), sub.getMotherAccount());
            if (motherAccountByEac.getCode() == ResultEntity.SUCCESS) {
                MotherAccountInfoModel account = JSON.parseObject(motherAccountByEac.getData().toString(), MotherAccountInfoModel.class);
                txService.singleQryOrder(account, sub.getCoinCode() + "_" + sub.getCoinCurrency(), sub.getBillNo());
            } else {
                log.error("Query Exchange's Account was Failed,The Reason Is:{}", motherAccountByEac.getMessage());
            }
        } catch (Exception e) {
            log.error("接收同步子委托MQ时出现异常:{}", e.getMessage());
        }
    }


    @RabbitListener(queues = "#{waitSendSubDelegateFailureQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handlerFailureSubDelegate(Map<String, Object> params) {
        if (null == params) {
            return;
        }
        String msg = params.get("msg").toString();
        SubDelegation sub = JSON.parseObject(params.get("subDelegation").toString(), SubDelegation.class);
        try {
            if (sub.getStyle() == SubDelegation.STYLE.LIMITED.code) {
                if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handlerFailedOrderOfLimitedBuyIn(sub, msg);
                } else {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handlerFailedOrderOfLimitedSellOut(sub, msg);
                }
            } else {
                if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handlerFailedOrderOfMarketBuyIn(sub, msg);
                } else {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handlerFailedOrderOfMarketSellOut(sub, msg);
                }
            }
        } catch (Exception e) {
            log.error("处理子委托失败", e.getMessage());
            sub.setState(SubDelegation.STATE.FAILED.code);
            sub.setInfo(e.getMessage());
            subDelegationService.updateByPrimaryKeySelective(sub);
        }

    }

    @RabbitListener(queues = "#{waitSendSubDelegateSuccessQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handlerSuccessSubDelegate(Map<String, Object> params) {
        if (null == params) {
            return;
        }
        String entrustNo = params.get("entrustNo").toString();
        OrderQryRes result = JSON.parseObject(params.get("result").toString(), OrderQryRes.class);
        SubDelegation sub = JSON.parseObject(params.get("subDelegation").toString(), SubDelegation.class);
        try {
            if (sub.getStyle() == SubDelegation.STYLE.LIMITED.code) {
                if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handleSucceedOrderOfLimitBuyIn(sub, result, entrustNo);
                } else {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handleSucceedOrderOfLimitSellOut(sub, result, entrustNo);
                }
            } else {
                if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handleSucceedOrderOfMarketBuyIn(sub, result, entrustNo);
                } else {
                    //1.生成成交记录并保存 2.子委托信息更新 3.主委托信息更新 4.增加持仓、扣除冻结
                    txOrderHandlerService.handleSucceedOrderOfMarketSellOut(sub, result, entrustNo);
                }
            }
        } catch (Exception e) {
            log.error("MQ成功处理子委托失败,{}" ,e.getMessage());
        }

    }

    @RabbitListener(queues = "#{waitSendRevokeDelegateFailureQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handlerFailureRevokeDelegate(Map<String, Object> params) {
        if (null == params) {
            return;
        }
        SubDelegation sub = JSON.parseObject(params.get("subDelegation").toString(), SubDelegation.class);
        String msg = params.get("msg").toString();
        try {
            //1.子委托信息更新 2.主委托信息更新 3.解冻资产
            if (sub.getStyle() == SubDelegation.STYLE.LIMITED.code) {
                if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                    txOrderHandlerService.handlerRevokeOrderOfLimitedBuyIn(sub, msg);
                } else {
                    txOrderHandlerService.handlerRevokeOrderOfLimitedSellOut(sub, msg);
                }
            } else {
                if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                    txOrderHandlerService.handlerRevokeOrderOfMarketBuyIn(sub, msg);
                } else {
                    txOrderHandlerService.handlerRevokeOrderOfMarketSellOut(sub, msg);
                }
            }
        } catch (Exception e) {
            log.error("MQ失败撤回处理子委托失败,{}" ,e.getMessage());
        }

    }

    @RabbitListener(queues = "#{waitSendMainDelegateFailureQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handlerFailureMainDelegate(MainDelegation delegate) {
        if (null == delegate) {
            return;
        }
        try {
            if (delegate.getStyle() == MainDelegation.STYLE.LIMITED.code) {
                if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
                    txOrderHandlerService.handleMainDelegateOfFailureByLimitedBuyIn(delegate);
                } else if (delegate.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
                    txOrderHandlerService.handleMainDelegateOfFailureByLimitedSellout(delegate);
                }
            } else if (delegate.getStyle() == MainDelegation.STYLE.MARKET.code) {
                if (delegate.getDirection() == MainDelegation.DIRECTION.BUYIN.code) {
                    txOrderHandlerService.handleMainDelegateOfFailureByMarketBuyIn(delegate);
                } else if (delegate.getDirection() == MainDelegation.DIRECTION.SELLOUT.code) {
                    txOrderHandlerService.handleMainDelegateOfFailureByMarketSellout(delegate);
                }
            }
        } catch (Exception e) {
            log.error("MQ失败处理主委托失败,{}" ,e.getMessage());
        }
    }
}

