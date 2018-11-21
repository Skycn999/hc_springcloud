package com.mi.hundsun.oxchains.quartz.task.handler;

import com.mi.hundsun.oxchains.base.common.enums.ExchangeEnum;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.model.user.UserCountModel;
import com.mi.hundsun.oxchains.base.core.po.count.CountEarnings;
import com.mi.hundsun.oxchains.base.core.po.count.CountTransaction;
import com.mi.hundsun.oxchains.base.core.service.count.CountEarningsService;
import com.mi.hundsun.oxchains.base.core.service.count.CountTransactionService;
import com.mi.hundsun.oxchains.base.core.service.tx.DealOrderService;
import com.mi.hundsun.oxchains.base.core.service.user.UsersService;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 统计服务处理器
 *
 * @ClassName: CountServiceHandler
 * @date 2018-04-24 10:49:34
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class CountServiceHandler {

    @Autowired
    private CountTransactionService countTransactionService;

    @Autowired
    private DealOrderService dealOrderService;

    @Autowired
    private CountEarningsService countEarningsService;

    @Autowired
    private UsersService usersService;

    public void countTransInfo() {
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
    }

    public void countTransServiceInfo() {
        List<CountDealOrderModel> orders = dealOrderService.countServiceFeeInfo();
        //循环列表 生成记录
        for (CountDealOrderModel order : orders) {
            //查询是否存在该时间点的数据
            CountEarnings earnings = new CountEarnings();
            Date createTime = order.getCreateTime();
            earnings.setCountTimePoint(createTime);
            earnings.setDelFlag(CountTransaction.DELFLAG.NO.code);
            CountEarnings e = countEarningsService.selectOne(earnings);
            if (null == e) {
                earnings.setUuid(RandomUtils.randomCustomUUID());
                earnings.setAmountTx(order.getGmv());
                earnings.setServiceFeeTx(order.getPlatFee());
                countEarningsService.insert(earnings);
            } else {
                e.setAmountTx(order.getGmv());
                e.setServiceFeeTx(order.getPlatFee());
                countEarningsService.updateByPrimaryKeySelective(e);
            }
        }
    }


    public void countUserInfo() {
        UserCountModel userCountModel = usersService.countUserInfo();
        if(null != userCountModel) {
            
        }
    }

}
