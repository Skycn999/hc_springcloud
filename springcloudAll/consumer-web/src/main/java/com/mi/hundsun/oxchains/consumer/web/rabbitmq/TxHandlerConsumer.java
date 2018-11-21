/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.enums.ExchangeEnum;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import com.mi.hundsun.oxchains.consumer.web.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.web.service.tx.TxInterface;
import com.mi.hundsun.oxchains.consumer.web.service.user.ExchangeInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 枫亭
 * @description 用户相关处理队列 - 消费者
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class TxHandlerConsumer {

    @Autowired
    private TxInterface txInterface;
    @Autowired
    private AccountInterface accountInterface;
    @Autowired
    private ExchangeInterface exchangeInterface;



    @RabbitListener(queues = "#{waitSendTxSellOutQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleTxSellOut(Map<String, Object> params) {
        if (null == params) {
            return;
        }
        String exchangeNo = params.get("exchangeNo").toString();
        String currencyPair = params.get("currencyPair").toString();
        MainDelegation delegation = JSON.parseObject(params.get("delegation").toString(), MainDelegation.class);
        try {
            String ziCode = TxUtils.getZiCode(currencyPair);
            List<MotherAccountInfoModel> models = new ArrayList<>();
            //交易所是全部 表明用户选择的是主流币种的卖出操作，这时候需要拆单卖出
            if (exchangeNo.equalsIgnoreCase(ExchangeEnum.ALL.getCode()) || TxUtils.isMainCoinCode(ziCode)) {
                ResultEntity motherAccountList = exchangeInterface.findMotherAccounts(exchangeNo, ziCode);
                if (motherAccountList.getCode() == ResultEntity.SUCCESS) {
                    models = JSON.parseArray(motherAccountList.getData().toString(), MotherAccountInfoModel.class);
                }
            } else {
                Account account = new Account();
                account.setExchangeNo(exchangeNo);
                account.setCoinCode(ziCode);
                account.setUserId(delegation.getUserId());
                account.setDelFlag(Account.DELFLAG.NO.code);
                account = accountInterface.selectOne(account);
                if (null == account) {
                    throw new BussinessException("用户无可用持仓,不能卖出");
                }
                //交易所等于某个具体的交易 则表示有持仓资产，不需要拆单到其他交易所
                ResultEntity motherAccountByEac = exchangeInterface.findMotherAccountByExNoAndAccountName(exchangeNo, account.getMotherAccount());
                if (motherAccountByEac.getCode() == ResultEntity.SUCCESS) {
                    MotherAccountInfoModel e = JSON.parseObject(motherAccountByEac.getData().toString(), MotherAccountInfoModel.class);
                    models.add(e);
                }
            }
            if (models.size() < 1) {
                throw new BussinessException("未找到合适的母账号");
            }
            //拆单交易执行 - 调用交易模块[执行交易]方法 返回执行结果
            params.clear();
            params.put("mainDelegation", delegation);
            params.put("accountInfoModels", models);
            params.put("exchangeNo", exchangeNo);
            txInterface.doTx(JSON.toJSONString(params));
        } catch (Exception e) {
            log.error("卖出拆单线程执行失败,失败原因: {}", e.getMessage());
            if (e instanceof BussinessException) {
                delegation.setRemark("卖出拆单线程执行失败,原因:" + e.getMessage());
            } else {
                delegation.setRemark("卖出拆单线程执行失败,原因:系统异常");
            }
            delegation.setExchangeNo(exchangeNo);
            txInterface.handleMainDelegateOfFailure(delegation);
        }
    }

    @RabbitListener(queues = "#{waitSendTxBuyInQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleTxBuyIn(Map<String, Object> params) {
        if (null == params) {
            return;
        }
        String exchangeNo = params.get("exchangeNo").toString();
        String currencyPair = params.get("currencyPair").toString();
        MainDelegation delegation = JSON.parseObject(params.get("delegation").toString(), MainDelegation.class);
        try {
            String muCode = TxUtils.getMuCode(currencyPair);
            List<MotherAccountInfoModel> models = new ArrayList<>();
            if (exchangeNo.equalsIgnoreCase(ExchangeEnum.ALL.getCode()) || TxUtils.isMainCoinCode(muCode)) {
                ResultEntity motherAccountList = exchangeInterface.findMotherAccounts(exchangeNo, muCode);
                if (motherAccountList.getCode() == ResultEntity.SUCCESS) {
                    models = JSON.parseArray(motherAccountList.getData().toString(), MotherAccountInfoModel.class);
                }
            } else {
                Account account = new Account();
                account.setExchangeNo(exchangeNo);
                account.setCoinCode(muCode);
                account.setUserId(delegation.getUserId());
                account.setDelFlag(Account.DELFLAG.NO.code);
                account = accountInterface.selectOne(account);
                if (null == account) {
                    throw new BussinessException("用户无可用持仓,不能卖出");
                }
                //交易所等于某个具体的交易 则表示有持仓资产，不需要拆单到其他交易所
                ResultEntity motherAccountByEac = exchangeInterface.findMotherAccountByExNoAndAccountName(exchangeNo, account.getMotherAccount());
                if (motherAccountByEac.getCode() == ResultEntity.SUCCESS) {
                    MotherAccountInfoModel e = JSON.parseObject(motherAccountByEac.getData().toString(), MotherAccountInfoModel.class);
                    models.add(e);
                }
            }
            if (models.size() < 1) {
                throw new BussinessException("未找到合适的母账号");
            }
            //拆单交易执行 - 调用交易模块[执行交易]方法 返回执行结果
            params.clear();
            params.put("mainDelegation", delegation);
            params.put("accountInfoModels", models);
            params.put("exchangeNo", exchangeNo);
            txInterface.doTx(JSON.toJSONString(params));
        } catch (Exception e) {
            log.error("买入拆单线程执行失败,失败原因: {}", e.getMessage());
            if (e instanceof BussinessException) {
                delegation.setRemark("买入拆单线程执行失败,失败原因,原因:" + e.getMessage());
            } else {
                delegation.setRemark("买入拆单线程执行失败,失败原因,原因:系统异常");
            }
            //处理主订单
            txInterface.handleMainDelegateOfFailure(delegation);
        }
    }



}

