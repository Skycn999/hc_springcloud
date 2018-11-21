package com.mi.hundsun.oxchains.provider.trade.service;

import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;

/**
 * @author 枫亭
 * @description 交易订单成功或者失败处理service
 * @date 2018-05-24 10:48.
 */
public interface TxOrderHandlerService {

    /**
     * 市价卖出成功后续处理
     *
     * @param sub        子委托
     * @param result     查询结果
     * @param entrustNo  委托编号
     */
    void handleSucceedOrderOfMarketSellOut(SubDelegation sub, OrderQryRes result, String entrustNo);

    /**
     * 市价买入成功后续处理
     *
     * @param sub        子委托
     * @param result     查询结果
     * @param entrustNo  委托编号
     */
    void handleSucceedOrderOfMarketBuyIn(SubDelegation sub, OrderQryRes result, String entrustNo);

    /**
     * 限价卖出成功后续处理
     *
     * @param sub        子委托
     * @param result     查询结果
     * @param entrustNo  委托编号
     */
    void handleSucceedOrderOfLimitSellOut(SubDelegation sub, OrderQryRes result, String entrustNo);

    /**
     * 限价买入成功后续处理
     *
     * @param sub        子委托
     * @param result     查询结果
     * @param entrustNo  委托编号
     */
    void handleSucceedOrderOfLimitBuyIn(SubDelegation sub, OrderQryRes result, String entrustNo);

    /**
     * 限价买入失败 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerFailedOrderOfLimitedBuyIn(SubDelegation sub, String msg);

    /**
     * 限价卖出失败 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerFailedOrderOfLimitedSellOut(SubDelegation sub, String msg);

    /**
     * 市价买入失败 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerFailedOrderOfMarketBuyIn(SubDelegation sub, String msg);

    /**
     * 市价卖出失败 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerFailedOrderOfMarketSellOut(SubDelegation sub, String msg);

    /**
     * 限价买入主委托失败 后续操作 1.更新主委托  退回手续费和资产
     * @param delegation 主委托
     */
    void handleMainDelegateOfFailureByLimitedBuyIn(MainDelegation delegation);

    /**
     * 限价卖出主委托失败 后续操作 1.更新主委托  退回手续费和资产
     * @param delegation 主委托
     */
    void handleMainDelegateOfFailureByLimitedSellout(MainDelegation delegation);

    /**
     * 市价买入主委托失败 后续操作 1.更新主委托  退回手续费和资产
     * @param delegation 主委托
     */
    void handleMainDelegateOfFailureByMarketBuyIn(MainDelegation delegation);

    /**
     * 市价卖出主委托失败 后续操作 1.更新主委托  退回手续费和资产
     * @param delegation 主委托
     */
    void handleMainDelegateOfFailureByMarketSellout(MainDelegation delegation);

    /**
     * 限价买入撤单 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerRevokeOrderOfLimitedBuyIn(SubDelegation sub, String msg);

    /**
     * 限价卖出撤单 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerRevokeOrderOfLimitedSellOut(SubDelegation sub, String msg);

    /**
     * 市价买入撤单 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerRevokeOrderOfMarketBuyIn(SubDelegation sub, String msg);

    /**
     * 市价卖出撤单 后续处理
     *
     * @param sub 子委托
     * @param msg 失败原因
     */
    void handlerRevokeOrderOfMarketSellOut(SubDelegation sub, String msg);
}
