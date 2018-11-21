package com.mi.hundsun.oxchains.base.core.service.tx;

import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import com.mi.hundsun.oxchains.base.core.tx.po.DealOrder;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;

import java.util.List;
import java.util.Map;

/**
 * 交易管理-成交订单业务相关Service接口<br>
 *
 * @author bin
 * @date 2018-04-13 09:54:58
 */
public interface DealOrderService extends GenericService<Integer, DealOrder> {

    /**
     * 我的成交列表
     * @param userId      用户id
     * @param direction   委托方向
     * @param pageNumber  页码
     * @param pageSize    每页条数
     * @return  Map<String, Object>
     */
    Map<String, Object> getMyDealOrders(Integer userId, Integer direction, Integer pageNumber, Integer pageSize);

    /**
     * 生成成交订单
     * @param sub       子委托
     * @param result    查询结果
     * @param entrustNo 委托编号
     * @return DealOrder
     */
    List<DealOrder> generateForLimited(SubDelegation sub, OrderQryRes result, String entrustNo);

    /**
     * 市价买入成交 生成成交订单
     * @param sub         子委托
     * @param result      委托结果
     * @param entrustNo   委托编号
     * @return DealOrder
     */
    List<DealOrder> generateForMarket(SubDelegation sub, OrderQryRes result, String entrustNo);

    /**
     * 统计交易信息 按照交易所分组
     * @return List<CountDealOrderModel>
     */
    List<CountDealOrderModel> countGroupByExchange();

    /**
     * 统计交易信息 按照币种分组
     * @return List<CountDealOrderModel>
     */
    List<CountDealOrderModel> countGroupByCode();

    /**
     * 统计买入卖出次数
     * @return List<CountDealOrderModel>
     */
    List<CountDealOrderModel> countDirection();

    /**
     * 统计交易手续费 按照日期
     * @return List<CountDealOrderModel>
     */
    List<CountDealOrderModel> countServiceFeeInfo();

    /**
     * 统计当天交易量
     */
    List<CountDealOrderModel> countTodayTxVolume();
}
