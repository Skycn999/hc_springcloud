/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.trade.service;

import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.quote.Depth;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 枫亭
 * @description 交易相关服务
 * @date 2018-05-04 11:42.
 */
public interface TxService {

    /**
     * 买卖方向 1:买入,2:卖出
     */
    String ENTRUST_BS_1 = "1";
    /**
     * 买卖方向 1:买入,2:卖出
     */
    String ENTRUST_BS_2 = "2";

    /**
     * 委托类型 F1:市价委托,F2:限价委托
     */
    String ENTRUST_PROP_1 = "F1";
    /**
     * 委托类型 F1:市价委托,F2:限价委托
     */
    String ENTRUST_PROP_2 = "F2";

    /**
     * 下单操作
     *
     * @param delegation   主委托信息
     * @param exchangeNo   下单交易所编号
     * @param price        下单价格
     * @param amount       下单数量
     * @param currencyPair 交易对
     * @param account      交易母账号
     * @param style        委托类型 市价还是限价
     */
    String doOrderByLimited(MainDelegation delegation, String exchangeNo, BigDecimal price, BigDecimal amount,
                            String currencyPair, MotherAccountInfoModel account, Integer style);

    String doOrderByMarket(MainDelegation delegation, String exchangeNo, BigDecimal gmv, String currencyPair,
                           MotherAccountInfoModel account, Integer style);

    /**
     * 市价买入拆单算法
     *
     * @param depth             买卖10档行情
     * @param delegate          主委托信息
     * @param accountInfoModels 最优母账号列表(每个交易所一个)
     */
    void separateOrderByMarketPriceOfBuyIn(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels);

    /**
     * 市价卖出拆单算法
     *
     * @param depth             买卖10档行情
     * @param delegate          主委托信息
     * @param accountInfoModels 最优母账号列表(每个交易所一个)
     */
    void separateOrderByMarketPriceOfSellOut(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels);

    /**
     * 限价买入拆单算法
     *
     * @param depth             买卖10档行情
     * @param delegate          主委托信息
     * @param accountInfoModels 最优母账号列表(每个交易所一个)
     */
    void separateOrderByLimitPriceOfBuyIn(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels);

    /**
     * 限价卖出拆单算法
     *
     * @param depth             买卖10档行情
     * @param delegate          主委托信息
     * @param accountInfoModels 最优母账号列表(每个交易所一个)
     */
    void separateOrderByLimitPriceOfSellOut(Depth depth, MainDelegation delegate, List<MotherAccountInfoModel> accountInfoModels);

    /**
     * 查询订单并处理资产
     *
     * @param account 交易母账号
     * @param symbol  交易对
     * @param billNo  委托编号
     */
    void singleQryOrder(MotherAccountInfoModel account, String symbol, String billNo);

    /**
     * 撤回委托
     *
     * @param subDelegations 带撤回子委托
     * @param symbol         交易对
     */
    String revoke(List<SubDelegation> subDelegations, String symbol);


}
