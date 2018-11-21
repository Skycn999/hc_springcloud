/*
 * Copyright (c) 2015-2018, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.quartz.job;

import com.jbc.quartz.BaseQuartzJob;
import com.mi.hundsun.oxchains.base.common.enums.ExchangeEnum;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.po.count.CountTransaction;
import com.mi.hundsun.oxchains.base.core.service.count.CountTransactionService;
import com.mi.hundsun.oxchains.base.core.service.tx.DealOrderService;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @author 枫亭
 * @date 2018-06-14 14:15.
 */
public class CountTransInfoJob extends BaseQuartzJob {

    @Autowired
    private CountTransactionService countTransactionService;

    @Autowired
    private DealOrderService dealOrderService;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        try {
            List<CountDealOrderModel> orders = dealOrderService.countGroupByCode();
            //循环列表 生成记录
            for (CountDealOrderModel order : orders) {
                //查询是否存在该时间点的数据
                Date createTime = order.getCreateTime();
                CountTransaction transaction = new CountTransaction();
                transaction.setCountTimePoint(createTime);
                transaction.setExNo(order.getExchange());
                transaction.setDelFlag(CountTransaction.DELFLAG.NO.code);
                CountTransaction trans = countTransactionService.selectOne(transaction);
                if (null == trans) {
                    transaction.setUuid(RandomUtils.randomCustomUUID());
                    transaction.setName(ExchangeEnum.valueOf(order.getExchange()).name());
                    transaction.setVolumeTx(order.getAmount());
                    transaction.setTotalTransactions(order.getTotalTransactions().intValue());
                    transaction.setAmountTx(order.getGmv());
                    transaction.setCode(order.getCoinCode() + "_" + order.getCoinCurrency());
                    countTransactionService.insert(transaction);
                } else {
                    trans.setVolumeTx(order.getAmount());
                    trans.setTotalTransactions(order.getTotalTransactions().intValue());
                    trans.setAmountTx(order.getGmv());
                    countTransactionService.updateByPrimaryKeySelective(trans);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
