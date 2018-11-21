package com.mi.hundsun.oxchains.base.core.service.count.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.count.CountTransactionMapper;
import com.mi.hundsun.oxchains.base.core.po.count.CountTransaction;
import com.mi.hundsun.oxchains.base.core.service.count.CountTransactionService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 交易汇总业务相关Service接口实现<br>
 *
 * @author bin
 * @ClassName: CountTransactionServiceImpl
 * @date 2018-04-24 10:49:34
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class CountTransactionServiceImpl implements CountTransactionService {

    @Resource
    private CountTransactionMapper countTransactionMapper;

//    @Autowired
//    private DealOrderService dealOrderService;
//
//    @Autowired
//    private CountEarningsService countEarningsService;
//
//    public void countTransInfo() {
//        List<CountDealOrderModel> orders = dealOrderService.countDealInfo();
//        //循环列表 生成记录
//        for (CountDealOrderModel order : orders) {
//            //查询是否存在该时间点的数据
//            Date createTime = order.getCreateTime();
//            CountTransaction transaction = new CountTransaction();
//            transaction.setCountTimePoint(createTime);
//            transaction.setExNo(order.getExchange());
//            transaction.setDelFlag(CountTransaction.DELFLAG.NO.code);
//            CountTransaction trans = countTransactionMapper.selectOne(transaction);
//            if (null == trans) {
//                transaction.setUuid(RandomUtils.randomCustomUUID());
//                transaction.setName(ExchangeEnum.valueOf(order.getExchange()).name());
//                transaction.setVolumeTx(order.getAmount());
//                transaction.setTotalTransactions(order.getTotalTransactions().intValue());
//                transaction.setAmountTx(order.getGmv());
//                transaction.setCode(order.getCoinCode() + "_" + order.getCoinCurrency());
//                countTransactionMapper.insert(transaction);
//            } else {
//                trans.setVolumeTx(order.getAmount());
//                trans.setTotalTransactions(order.getTotalTransactions().intValue());
//                trans.setAmountTx(order.getGmv());
//                countTransactionMapper.updateByPrimaryKeySelective(trans);
//            }
//        }
//    }
//
//    public void countTransServiceInfo() {
//        List<CountDealOrderModel> orders = dealOrderService.countServiceFeeInfo();
//        //循环列表 生成记录
//        for (CountDealOrderModel order : orders) {
//            //查询是否存在该时间点的数据
//            CountEarnings earnings = new CountEarnings();
//            Date createTime = order.getCreateTime();
//            earnings.setCountTimePoint(createTime);
//            earnings.setDelFlag(CountTransaction.DELFLAG.NO.code);
//            CountEarnings e = countEarningsService.selectOne(earnings);
//            if (null == e) {
//                earnings.setUuid(RandomUtils.randomCustomUUID());
//                earnings.setAmountTx(order.getGmv());
//                earnings.setServiceFeeTx(order.getPlatFee());
//                countEarningsService.insert(earnings);
//            } else {
//                e.setAmountTx(order.getGmv());
//                e.setServiceFeeTx(order.getPlatFee());
//                countEarningsService.updateByPrimaryKeySelective(e);
//            }
//        }
//    }

    @Override
    public GenericMapper<CountTransaction, Integer> _getMapper() {
        return countTransactionMapper;
    }

}
